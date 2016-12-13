package com.work189.msrpc.core.config.springboot.schema;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.work189.msrpc.core.common.exception.RegistryException;
import com.work189.msrpc.core.config.springboot.schema.entity.ConsumerEntity;
import com.work189.msrpc.core.config.springboot.service.SpringbootInitializeBean;
import com.work189.msrpc.core.registry.RegistryCenter;
import com.work189.msrpc.core.rpc.exchange.host.DefaultExchangeHostConfig;
import com.work189.msrpc.core.rpc.proxy.ProxyFactory;
import com.work189.msrpc.core.rpc.proxy.factory.javassist.JavassistProxyFactory;

public class ConsumerBean <T> extends ConsumerEntity<T> implements FactoryBean<T>, ApplicationContextAware, InitializingBean, DisposableBean{

	private static final long serialVersionUID = -7086918812180264355L;
	protected RegistryCenter registryCenter = RegistryCenter.master;
	
	@Override
	public void destroy() throws Exception {
		//consumer取消服务
		registryCenter.getRegistry().unsubscribe(proxyBeanDefine);
		registryCenter.getConsumerBeans().remove(proxyBeanDefine.getServiceId());
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		try{
			proxyBeanDefine.setPcFlag("consumer");
			proxyBeanDefine.setGroup(this.getWkGroup());
			proxyBeanDefine.setVersion(this.getWkVersion());
			proxyBeanDefine.setInterfaceClass(this.interfaceClass);
			proxyBeanDefine.setInterfaceName(this.getWkInterface());
			proxyBeanDefine.setObject( getObject() );
			proxyBeanDefine.setHostIp( DefaultExchangeHostConfig.config.getHostIp() );
			proxyBeanDefine.setHostPort( DefaultExchangeHostConfig.config.getHostPort() );
			proxyBeanDefine.setActiveKey( DefaultExchangeHostConfig.config.getActiveKey() );
			//consumer订阅服务
			registryCenter.getRegistry().subscribe(proxyBeanDefine);
		}catch(RegistryException e){
			
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringbootInitializeBean.setApplicationContext(applicationContext);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getObject() throws Exception {
		if(this.ref == null){
			ProxyFactory proxyFactory = new JavassistProxyFactory();
			this.ref = (T) proxyFactory.refer(proxyBeanDefine);
		}
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
