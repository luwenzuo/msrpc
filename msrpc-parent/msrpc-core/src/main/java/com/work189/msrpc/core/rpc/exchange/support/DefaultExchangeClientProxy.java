package com.work189.msrpc.core.rpc.exchange.support;

import java.lang.reflect.Method;
import java.util.List;

import com.work189.msrpc.core.common.exception.RegistryException;
import com.work189.msrpc.core.registry.RegistryCenter;
import com.work189.msrpc.core.registry.center.RegistryServer;
import com.work189.msrpc.core.rpc.exchange.ExchangeManager;
import com.work189.msrpc.core.rpc.exchange.buffer.ExchangeRequest;
import com.work189.msrpc.core.rpc.exchange.buffer.ExchangeResponse;
import com.work189.msrpc.core.rpc.exchange.buffer.rpcbuffer.RpcBuffer;
import com.work189.msrpc.core.rpc.exchange.host.ExchangeHostHander;
import com.work189.msrpc.core.rpc.protocol.encode.ProtocolCodecFactory;
import com.work189.msrpc.core.rpc.protocol.message.MessageFactory;
import com.work189.msrpc.core.rpc.protocol.message.MessageFactoryUtil;
import com.work189.msrpc.core.rpc.protocol.message.impl.MessageFactoryDefault;
import com.work189.msrpc.core.rpc.proxy.bean.ProxyBeanDefine;
import com.work189.msrpc.core.rpc.proxy.factory.AbstractProxyInvoker;
import com.work189.msrpc.core.transport.Client;
import com.work189.msrpc.core.transport.channel.Channel;
import com.work189.msrpc.core.transport.channel.support.DefaultChannelHanderClient;
import com.work189.msrpc.core.transport.channel.swap.ChannelFuture;

public class DefaultExchangeClientProxy extends AbstractProxyInvoker {
	private RegistryCenter registryCenter = RegistryCenter.master;

	public DefaultExchangeClientProxy(ProxyBeanDefine proxyBeanDefine) {
		super(proxyBeanDefine);
	}

	@Override
	public Object doInvoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		//通讯操作句柄
		DefaultChannelHanderClient channelHander = new DefaultChannelHanderClient();
		if (!proxyBeanDefine.isOk()) {
			RegistryServer.checkService();
			throw new RuntimeException("提供服务的未就绪"+proxyBeanDefine.getServiceId());
		}
		
		Client client = null;
		ExchangeResponse exchangeResponse = null;
		ExchangeRequest exchangeRequest = new ExchangeRequest();
		MessageFactory requestFactory = new MessageFactoryDefault();
		MessageFactory responseFactory = new MessageFactoryDefault();
		ExchangeHostHander host = new ExchangeHostHander();
		List<ProtocolCodecFactory> protocolCodecs = ExchangeManager.getProtocolCodecs();
		
		//服务器地址
		host.setHostAddress(proxyBeanDefine.getServiceHostIp(), proxyBeanDefine.getServiceHostPort());
		
		//消息编号
		ChannelFuture channelFuture = ChannelFuture.newFuture();
		channelFuture.hostKey = host.getHostKey();
		requestFactory.setId(channelFuture.requestId);

		//请求数据
		exchangeRequest.setInputArgsType( method.getParameterTypes() );
		exchangeRequest.setInputArgsValues( args );
		exchangeRequest.setMethodName( method.getName() );
		exchangeRequest.setServiceId( proxyBeanDefine.getServiceId() );
		exchangeRequest.setServiceName( proxyBeanDefine.getInterfaceName() );

		//请求数据转码
		MessageFactoryUtil.setServiceBufferData(requestFactory, exchangeRequest);
		for (ProtocolCodecFactory codec : protocolCodecs) {
			codec.getEncoder().encode(requestFactory);
		}

		//通讯数据
		RpcBuffer requestBuffer = MessageFactoryUtil.packBuffer(requestFactory);
		
		//向服务器发起请求
		client = ExchangeManager.getClient(host, channelHander);
		Channel channel = client.getChannel();
		if(channel != null){
			channel.send(requestBuffer, true);
		}else{
			//无可用发送通道,设置服务不可用,取消订阅
			registryCenter.getRegistry().unsubscribe(proxyBeanDefine);
		}
		
		//等待服务器响应
		Object retObject = channelFuture.get();

		//响应数据转码
		MessageFactoryUtil.readBuffer(responseFactory, (RpcBuffer)retObject );
		for (ProtocolCodecFactory codec : protocolCodecs) {
			codec.getDecoder().decode(requestFactory);
		}
		exchangeResponse = (ExchangeResponse) MessageFactoryUtil.getServiceBufferData(responseFactory);
		

		//处理异常
		if(exchangeResponse.getThrowableObject() != null){
			Throwable throwable = new RuntimeException();
			try{
				throwable = (Throwable) exchangeResponse.getThrowableObject();
				

				if(exchangeResponse.getThrowableObject() instanceof RegistryException){
					//服务器提供的服务不存在,设置为客户端无效,需要重新引用
					registryCenter.getRegistry().unsubscribe(proxyBeanDefine);
				}
			}catch(Throwable e){
				throwable = new RuntimeException("系统异常无法识别\r\n"+exchangeResponse.getThrowableMessage());
			}
			throw throwable;
		}

		//如果远程服务不存在,则标记远程服务不存在
		return exchangeResponse.getResponseObject();
	}

}
