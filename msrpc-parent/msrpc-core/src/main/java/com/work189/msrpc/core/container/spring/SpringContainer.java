package com.work189.msrpc.core.container.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.work189.msrpc.core.common.log.LoggerFactory;
import com.work189.msrpc.core.container.Container;

public class SpringContainer implements Container{
	
	public static final String SPRING_CONFIG = "xxsys.spring.config";
	public static final String DEFAULT_SPRING_CONFIG = "classpath*:META-INF/msrpc.spring/*.xml";
	private static ClassPathXmlApplicationContext context=null;

	public static ClassPathXmlApplicationContext getContext() {
		return context;
	}

	@Override
	public void start() {
		String configPath = DEFAULT_SPRING_CONFIG;
        context = new ClassPathXmlApplicationContext(configPath.split("[,\\s]+"));
        context.start();
	}

	@Override
	public void stop() {
		try{
			if(context != null){
				context.stop();
				context.close();
				context = null;
			}
		}catch(Throwable e){
			LoggerFactory.getLogger(SpringContainer.class).error(e);
		}
	}
}
