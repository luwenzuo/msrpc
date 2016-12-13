package com.work189.msrpc.core.registry.support;

import com.work189.msrpc.core.common.log.Logger;
import com.work189.msrpc.core.common.log.LoggerFactory;
import com.work189.msrpc.core.registry.Registry;
import com.work189.msrpc.core.registry.RegistryBean;
import com.work189.msrpc.core.registry.RegistryCenter;
import com.work189.msrpc.core.rpc.exchange.ExchangeManager;
import com.work189.msrpc.core.rpc.exchange.host.ExchangeHostHander;
import com.work189.msrpc.core.rpc.proxy.bean.ProxyBeanDefine;
import com.work189.msrpc.core.transport.channel.support.DefaultChannelHanderClient;

public abstract class AbstractRegistry implements Registry{
	private final Logger logger = LoggerFactory.getLogger(AbstractRegistry.class);
	
	public abstract void nodifyHostError(String hostKey);

	@Override
	public void register(ProxyBeanDefine bean) {
		if(bean == null){
			return;
		}
		bean.setOk(true);
		
		//本地登记consumer服务
		if(!RegistryBean.m_provider_service_map.containsKey(bean.getServiceId())){
			bean.setOk(true);
			RegistryBean.m_provider_service_map.put(bean.getServiceId(), bean);
		}else{
			ProxyBeanDefine containerBean = RegistryBean.m_consumer_service_map.get(bean.getServiceId());
			containerBean.setOk(true);
		}

		logger.info("发布服务:"+bean.getServiceId()+"["+bean.getHostKey()+"]");
		System.out.println("发布服务:"+bean.getServiceId()+"["+bean.getHostKey()+"]");
	}

	@Override
	public void unregister(ProxyBeanDefine bean) {
		if(bean == null){
			return;
		}
		bean.setOk(false);
		
		//本地移除服务
		ProxyBeanDefine containerBean = RegistryBean.m_provider_service_map.get(bean.getServiceId());
		containerBean.setOk(false);

		logger.info("注销服务"+bean.getServiceId()+"["+bean.getHostKey()+"]");
		System.out.println("注销服务"+bean.getServiceId()+"["+bean.getHostKey()+"]");
	}

	@Override
	public void subscribe(ProxyBeanDefine bean) {
		if(bean == null){
			return;
		}
		
		boolean readyFlag = false;

		try{
			//检查服务是否可用
			DefaultChannelHanderClient channelHander = new DefaultChannelHanderClient();
			ExchangeHostHander host = new ExchangeHostHander();
			host.setHostAddress(bean.getServiceHostIp(), bean.getServiceHostPort());
			ExchangeManager.getClient(host, channelHander).getChannel().toString();
			
			//设置本地服务可用
			readyFlag = true;
			
			//登记消费者主机
			RegistryBean.m_consumer_remote_host_map.put(bean.getServiceHostKey(), bean.getServiceHostKey());
		}catch(Throwable e){
			logger.error("订阅服务失败:"+bean.getServiceId());
			logger.debug(e);
			//异常服务不可用
			readyFlag = false;
			nodifyHostError(bean.getServiceHostKey());
		}
		
		//本地登记consumer服务
		if(!RegistryBean.m_consumer_service_map.containsKey(bean.getServiceId())){
			bean.setOk( readyFlag );
			RegistryBean.m_consumer_service_map.put(bean.getServiceId(), bean);
		}else{
			ProxyBeanDefine containerBean = RegistryBean.m_consumer_service_map.get(bean.getServiceId());
			containerBean.setOk( readyFlag );
		}
		
		logger.info("订阅服务:"+bean.getServiceId()+"["+bean.getServiceHostKey()+"]");
		System.out.println("订阅服务:"+bean.getServiceId()+"["+bean.getServiceHostKey()+"]");
	}

	@Override
	public void unsubscribe(ProxyBeanDefine bean) {
		if(bean == null){
			return;
		}
		bean.setOk(false);
		
		//设置本地服务不可用
		ProxyBeanDefine containerBean = RegistryBean.m_consumer_service_map.get(bean.getServiceId());
		containerBean.setOk(false);
		logger.info("取消订阅:"+bean.getServiceId()+"["+bean.getServiceHostKey()+"]");
		System.out.println("取消订阅:"+bean.getServiceId()+"["+bean.getServiceHostKey()+"]");
	}

	@Override
	public void lookup(ProxyBeanDefine bean) {
		subscribe(bean);
	}
	
	public boolean isServiceEmpty(){
		if( RegistryCenter.master.getConsumerBeans().isEmpty() &&
			RegistryCenter.master.getProviderBeans().isEmpty() ){
			return true;
		}
		return false;
	}
}
