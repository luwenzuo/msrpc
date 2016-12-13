package com.work189.msrpc.core.registry.center;

import java.util.Map;
import java.util.Map.Entry;

import com.work189.msrpc.core.registry.RegistryCenter;
import com.work189.msrpc.core.rpc.proxy.bean.ProxyBeanDefine;

public class RegistryCheckService {
	private static RegistryCenter registryCenter = RegistryCenter.master;

	public static synchronized void checkAllProviderServices() {
		
		// 锁等待
		Map<String, ProxyBeanDefine> providers = 
					registryCenter.getProviderBeans();
		for (Entry<String, ProxyBeanDefine> entry : providers.entrySet()) {
			ProxyBeanDefine bean = entry.getValue();
			if (!bean.isOk()) {
				// 注册服务
				registryCenter.getRegistry().register(bean);
			}
		}
	}

	public static synchronized void checkAllConsumerServices() {
		Map<String, ProxyBeanDefine> consumers = 
					registryCenter.getConsumerBeans();
		for (Entry<String, ProxyBeanDefine> entry : consumers.entrySet()) {
			ProxyBeanDefine bean = entry.getValue();
			if (!bean.isOk()) {
				// 订阅服务
				//registryCenter.getRegistry().subscribe(bean);
				try{
					registryCenter.getRegistry().lookup(bean);
				}catch(Throwable e){
					
				}
			}
		}
	}
	
	public static synchronized void clearAllService(){
		registryCenter.getConsumerBeans().clear();
		registryCenter.getProviderBeans().clear();
	}

}
