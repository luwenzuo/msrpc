package com.work189.msrpc.core.config.springboot.schema;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.work189.msrpc.core.common.exception.RegistryException;
import com.work189.msrpc.core.config.springboot.schema.entity.ProviderEntity;
import com.work189.msrpc.core.config.springboot.service.SpringbootInitializeBean;
import com.work189.msrpc.core.registry.RegistryCenter;
import com.work189.msrpc.core.rpc.exchange.host.DefaultExchangeHostConfig;
import com.work189.msrpc.core.rpc.proxy.ProxyFactory;
import com.work189.msrpc.core.rpc.proxy.factory.javassist.JavassistProxyFactory;

public class ProviderBean<T> extends ProviderEntity<T> implements FactoryBean<T>, ApplicationContextAware, InitializingBean, DisposableBean{

	private static final long serialVersionUID = 7515033650280934093L;
	protected RegistryCenter registryCenter = new RegistryCenter();

	@Override
	public void destroy() throws Exception {
		//provider注销服务
		registryCenter.getRegistry().unregister(proxyBeanDefine);
		registryCenter.getProviderBean( proxyBeanDefine.getServiceId() );
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try{
			this.ref = this.getWkRef();
			
			proxyBeanDefine.setPcFlag("provider");
			proxyBeanDefine.setObject(this.ref);
			proxyBeanDefine.setGroup(this.getWkGroup());
			proxyBeanDefine.setVersion(this.getWkVersion());
			proxyBeanDefine.setInterfaceClass(this.interfaceClass);
			proxyBeanDefine.setInterfaceName(this.getWkInterface());
			
			proxyBeanDefine.setHostIp( DefaultExchangeHostConfig.config.getHostIp() );
			proxyBeanDefine.setHostPort( DefaultExchangeHostConfig.config.getHostPort() );
			proxyBeanDefine.setActiveKey( DefaultExchangeHostConfig.config.getActiveKey() );
			
			proxyBeanDefine.setServiceHostIp( proxyBeanDefine.getHostIp() );
			proxyBeanDefine.setServiceHostKey( proxyBeanDefine.getHostKey() );
			proxyBeanDefine.setServiceHostPort( proxyBeanDefine.getHostPort() );
			
			//发布服务
			ProxyFactory proxyFactory = new JavassistProxyFactory();
			proxyFactory.export(proxyBeanDefine);
			//provider注册服务
			registryCenter.getRegistry().register(proxyBeanDefine);
		}catch(RegistryException e){
			
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringbootInitializeBean.setApplicationContext(applicationContext);
	}

	@Override
	public T getObject() throws Exception {
		return this.ref;
	}

	@Override
	public Class<?> getObjectType() {
		return this.ref.getClass();
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
