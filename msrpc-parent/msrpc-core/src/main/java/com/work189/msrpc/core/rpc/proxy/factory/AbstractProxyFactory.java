package com.work189.msrpc.core.rpc.proxy.factory;

import java.lang.reflect.InvocationHandler;

import com.work189.msrpc.core.registry.RegistryCenter;
import com.work189.msrpc.core.rpc.exchange.ExchangeManager;
import com.work189.msrpc.core.rpc.exchange.host.DefaultExchangeHostConfig;
import com.work189.msrpc.core.rpc.exchange.support.DefaultExchangeClientProxy;
import com.work189.msrpc.core.rpc.proxy.ProxyFactory;
import com.work189.msrpc.core.rpc.proxy.bean.ProxyBeanDefine;

public abstract class AbstractProxyFactory implements ProxyFactory{

	protected RegistryCenter registryCenter = RegistryCenter.master;

	@Override
	public void export(ProxyBeanDefine proxyBeanDefine) {
		ExchangeManager.getDefaultServerListen();
		proxyBeanDefine.setHostPort(DefaultExchangeHostConfig.config.getHostPort());
		proxyBeanDefine.setHostIp(DefaultExchangeHostConfig.config.getHostIp());
		proxyBeanDefine.setServiceHostIp( proxyBeanDefine.getHostIp() );
		proxyBeanDefine.setServiceHostPort( proxyBeanDefine.getHostPort() );
		/*

		//设置服务交换类
		ChannelHandler channelHander = new DefaultChannelHanderServer();

		// 启动服务器
		String hostId = proxyBeanDefine.getHostIp()+":"+proxyBeanDefine.getHostPort();
		if(registryCenter.getServer(hostId) == null){
			ExchangeHostHander host = new ExchangeHostHander();
			host.setHostAddress(proxyBeanDefine.getHostIp(), proxyBeanDefine.getHostPort());
			Server server = ExchangeManager.getServer(host, channelHander);
			registryCenter.addServer(hostId, server, proxyBeanDefine);
		}*/
	}

	@Override
	public Object refer(ProxyBeanDefine proxyBeanDefine) {
		ExchangeManager.getDefaultServerListen();
		
		//设置通讯类
		InvocationHandler invoker = new DefaultExchangeClientProxy( proxyBeanDefine );

		//设置代理类
		Object object = getProxyObject(proxyBeanDefine.getInterfaceClass(), invoker);
		proxyBeanDefine.setObject(object);

		return object;
	}
	
	public abstract Object getProxyObject(Class<?> interfaceClass, InvocationHandler invoker);

}
