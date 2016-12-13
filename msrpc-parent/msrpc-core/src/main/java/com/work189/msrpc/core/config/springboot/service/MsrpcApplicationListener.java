package com.work189.msrpc.core.config.springboot.service;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import com.work189.msrpc.core.container.initialize.SystemInitialize;

@SuppressWarnings("rawtypes")
public class MsrpcApplicationListener implements ApplicationListener{
	
	public MsrpcApplicationListener(){
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		
		//System.out.println("event4===="+event);
        if (event instanceof ContextClosedEvent) {
        	// 应用关闭 
        	ContextClosedEvent appEvent = (ContextClosedEvent) event;
        	if(appEvent.getApplicationContext().getParent() == null){
        		SystemInitialize.stop();
        	}
        }
        
		/*  if (event instanceof ApplicationEnvironmentPreparedEvent) { // 初始化环境变量 }
        else if (event instanceof ApplicationPreparedEvent) { // 初始化完成 }
        else if (event instanceof ContextRefreshedEvent) { // 应用刷新 }
        else if (event instanceof ApplicationReadyEvent) {// 应用已启动完成 }
        else if (event instanceof ContextStartedEvent) { // 应用启动，需要在代码动态添加监听器才可捕获 }
        else if (event instanceof ContextStoppedEvent) { // 应用停止 }
        else if (event instanceof ContextClosedEvent) { // 应用关闭 }
        else {}
        */
	}

}
