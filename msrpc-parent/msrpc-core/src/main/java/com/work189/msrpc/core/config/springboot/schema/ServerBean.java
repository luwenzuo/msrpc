package com.work189.msrpc.core.config.springboot.schema;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.work189.msrpc.core.config.springboot.schema.entity.ServerEntity;

public class ServerBean<T> extends ServerEntity<T> implements FactoryBean<T>, ApplicationContextAware, InitializingBean, DisposableBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4639227667982552664L;

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T getObject() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

}
