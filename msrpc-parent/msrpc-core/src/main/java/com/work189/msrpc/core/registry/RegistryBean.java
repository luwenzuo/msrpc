package com.work189.msrpc.core.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.work189.msrpc.core.registry.entity.RegistryHostEntity;
import com.work189.msrpc.core.rpc.proxy.bean.ProxyBeanDefine;
import com.work189.msrpc.core.transport.Server;

public class RegistryBean {

	//服务主机(侦听端口)
	public static Map<String, Server> m_server_map = new ConcurrentHashMap<>();
	//服务主机(主机信息)
	public static Map<String, RegistryHostEntity> m_server_host_map = new ConcurrentHashMap<>();
	//服务--提供者
	public static Map<String, ProxyBeanDefine> m_provider_service_map = new ConcurrentHashMap<>();
	//服务--消费者
	public static Map<String, ProxyBeanDefine> m_consumer_service_map = new ConcurrentHashMap<>();
	//服务--消费者--远程主机
	public static Map<String, String> m_consumer_remote_host_map = new ConcurrentHashMap<>();
	
}
