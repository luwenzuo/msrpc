package com.work189.msrpc.core.registry;

import java.util.Map;

import com.work189.msrpc.core.registry.entity.RegistryHostEntity;
import com.work189.msrpc.core.registry.support.mysql.MysqlRegistrySupport;
import com.work189.msrpc.core.rpc.proxy.bean.ProxyBeanDefine;
import com.work189.msrpc.core.transport.Server;

public class RegistryCenter {
	
	public static RegistryCenter master = new RegistryCenter();

	public RegistryCenter(){
		
	}
	
	public Registry getRegistry(){
		return new MysqlRegistrySupport();
	}
	
	public ProxyBeanDefine getSystemBean(String serviceId){
		return RegistryBean.m_provider_service_map.get(serviceId);
	}
	
	public ProxyBeanDefine getProviderBean(String serviceId){
		return RegistryBean.m_provider_service_map.get(serviceId);
	}
	public Map<String, ProxyBeanDefine> getProviderBeans(){
		return RegistryBean.m_provider_service_map;
	}
	
	public ProxyBeanDefine getConsumerBean(String serviceId){
		return RegistryBean.m_consumer_service_map.get(serviceId);
	}
	public Map<String, ProxyBeanDefine> getConsumerBeans( ){
		return RegistryBean.m_consumer_service_map;
	}
	
	public Server getServer(String hostId){
		return RegistryBean.m_server_map.get(hostId);
	}
	
	public void addServer(String hostId, Server server, RegistryHostEntity host){
		RegistryBean.m_server_map.put(hostId, server);
		
		/*RegistryHostEntity host = new RegistryHostEntity();
		host.setHostKey(proxyBeanDefine.getHostKey());
		host.setHostIP(proxyBeanDefine.getHostIp());
		host.setHostPort(proxyBeanDefine.getHostPort());
		host.setActiveKey(proxyBeanDefine.getActiveKey());*/
		host.setServer( server );
		RegistryBean.m_server_host_map.put(hostId, host);
	}

	public Map<String, RegistryHostEntity> getHosts(){
		return RegistryBean.m_server_host_map;
	}
}
