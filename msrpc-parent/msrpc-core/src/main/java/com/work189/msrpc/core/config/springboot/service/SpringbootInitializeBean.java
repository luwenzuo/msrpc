package com.work189.msrpc.core.config.springboot.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringbootInitializeBean {
	private static boolean initializeFlag = false;
	private static volatile ApplicationContext applicationContext;
    private static Map<String , Class<?> > beans = new ConcurrentHashMap<>();
    
    public static void close(){
    	if(applicationContext != null){
    		ConfigurableApplicationContext configurableApplicationContext =
    				(ConfigurableApplicationContext) applicationContext;
    		configurableApplicationContext.close();
    	}
    }
    
    public static void createBeanDefinition(ApplicationContext applicationContext, 
    		String beanName, Class<?> beanClass){
    	ConfigurableApplicationContext configurableApplicationContext =
    			(ConfigurableApplicationContext) applicationContext;
    	DefaultListableBeanFactory defaultListableBeanFactory = 
    			(DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
    	BeanDefinitionBuilder beanDefinitionBuilder = 
    			BeanDefinitionBuilder.genericBeanDefinition(beanClass);
    	//beanDefinitionBuilder.addPropertyValue("", "");
    	defaultListableBeanFactory.registerBeanDefinition(
    			beanName, beanDefinitionBuilder.getBeanDefinition());

    	defaultListableBeanFactory.getBean(beanName);
    	
    	//configurableApplicationContext.registerShutdownHook();
    }

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public synchronized static void setApplicationContext(ApplicationContext applicationContext) {
		if(SpringbootInitializeBean.applicationContext == null){
			SpringbootInitializeBean.applicationContext = applicationContext;
		}
		
		bootBean();
	}
	
	private static synchronized void bootBean() throws BeansException {
		if(applicationContext == null){
			return;
		}
		if(initializeFlag == true){
			return;
		}
		initializeFlag = true;

		//注册系统服务
		SpringbootInitializeBean.beans.put("msrpc.system.MsrpcApplicationListener", MsrpcApplicationListener.class);
		
		for(Entry<String, Class<?>> bean:beans.entrySet()){
			createBeanDefinition(applicationContext, bean.getKey(), bean.getValue());
		}
	}
}
