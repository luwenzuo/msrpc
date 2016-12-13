package com.work189.msrpc.core.rpc.exchange.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.work189.msrpc.core.common.exception.RegistryException;
import com.work189.msrpc.core.common.log.Logger;
import com.work189.msrpc.core.common.log.LoggerFactory;
import com.work189.msrpc.core.registry.RegistryCenter;
import com.work189.msrpc.core.rpc.exchange.ExchangeManager;
import com.work189.msrpc.core.rpc.exchange.buffer.ExchangeRequest;
import com.work189.msrpc.core.rpc.exchange.buffer.ExchangeResponse;
import com.work189.msrpc.core.rpc.exchange.buffer.rpcbuffer.RpcBuffer;
import com.work189.msrpc.core.rpc.protocol.encode.ProtocolCodecFactory;
import com.work189.msrpc.core.rpc.protocol.message.MessageFactory;
import com.work189.msrpc.core.rpc.protocol.message.MessageFactoryUtil;
import com.work189.msrpc.core.rpc.protocol.message.impl.MessageFactoryDefault;
import com.work189.msrpc.core.rpc.proxy.bean.ProxyBeanDefine;

public class DefaultExchangeServerHander extends AbstractExchangeHander{

	private RegistryCenter registryCenter = RegistryCenter.master;
	private final Logger logger = LoggerFactory.getLogger(DefaultExchangeServerHander.class);
	
	
	@SuppressWarnings("unused")
	@Override
	public RpcBuffer callService(RpcBuffer requestBuffer) {

		ProxyBeanDefine beanDefine = null;
		List<ProtocolCodecFactory> protocolCodecs = ExchangeManager.getProtocolCodecs();
		MessageFactory requestFactory = new MessageFactoryDefault();
		MessageFactory responseFactory = new MessageFactoryDefault();
		ExchangeRequest exchangeRequest = null;
		ExchangeResponse exchangeResponse = new ExchangeResponse();
		
		//请求数据转码
		MessageFactoryUtil.readBuffer(requestFactory, requestBuffer);
		for(ProtocolCodecFactory codec:protocolCodecs){
			codec.getDecoder().decode(requestFactory);
		}
		exchangeRequest = (ExchangeRequest) MessageFactoryUtil.getServiceBufferData(requestFactory);
		
		//设置过滤器
		/*{
			MessageReadUtil msgRead = new MessageReadUtil();
			msgRead.read( this.getRequestData().array() );
			List<ProtocolFilter> filters = FilterManager.getProtocolFilters();
			for(ProtocolFilter filter:filters){
				filter.filter( msgRead.getFields() );
			}
		}*/
		
		//系统服务-------------------------------------------------------------------------------------
		if( false ){
			beanDefine = registryCenter.getSystemBean( exchangeRequest.getServiceId() );
		}else{
			beanDefine = registryCenter.getProviderBean( exchangeRequest.getServiceId() );
		}
		if(beanDefine == null || !beanDefine.isOk()){
			logger.error("提供服务不存在"+exchangeRequest.getServiceId());
			return returnError(requestFactory.getId(), 
					new RegistryException("服务不存在"+exchangeRequest.getServiceId()));
		}
		
		try {
			//调用本地service
			Method method = beanDefine.getObject().getClass().getDeclaredMethod(exchangeRequest.getMethodName(), exchangeRequest.getInputArgsType());
			
			try{
				Object retObject = method.invoke(beanDefine.getObject(), exchangeRequest.getInputArgsValues());
				exchangeResponse.setResponseObject(retObject);
			}catch(InvocationTargetException e){
				exchangeResponse.setThrowableMessage(e.getMessage());
				exchangeResponse.setThrowableObject(e.getTargetException());
			}
			
			//响应数据转码
			MessageFactoryUtil.setServiceBufferData(responseFactory, exchangeResponse);
			for(ProtocolCodecFactory codec:protocolCodecs){
				codec.getEncoder().encode(responseFactory);
			}
			//按原消息编号响应数据
			responseFactory.setId( requestFactory.getId() );
			
			RpcBuffer responseBuffer = MessageFactoryUtil.packBuffer(responseFactory) ;
			return responseBuffer;
			
		} catch (Throwable e) {
			logger.error(e);
			throw new RuntimeException("JavassistProxyFactory.callService error");
		}
	}
	
	public RpcBuffer returnError(int id, Throwable e){

		ExchangeResponse exchangeResponse = new ExchangeResponse();
		
		exchangeResponse.setResponseObject(null);
		exchangeResponse.setThrowableMessage(e.getMessage());
		exchangeResponse.setThrowableObject(e);
		
		MessageFactory responseFactory = new MessageFactoryDefault();
		MessageFactoryUtil.setServiceBufferData(responseFactory, exchangeResponse);
		
		//按原消息编号响应数据
		responseFactory.setId( id );
		
		RpcBuffer responseBuffer = MessageFactoryUtil.packBuffer(responseFactory) ;
		return responseBuffer;
	}

}
