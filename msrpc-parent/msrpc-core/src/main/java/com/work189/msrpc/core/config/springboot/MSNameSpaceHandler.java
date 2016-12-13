package com.work189.msrpc.core.config.springboot;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.work189.msrpc.core.config.springboot.schema.ConsumerBean;
import com.work189.msrpc.core.config.springboot.schema.ProviderBean;
import com.work189.msrpc.core.config.springboot.schema.ServerBean;
import com.work189.msrpc.core.container.initialize.SystemInitialize;


public class MSNameSpaceHandler extends NamespaceHandlerSupport {
	public void init() {
		//系统入口初始化
		SystemInitialize.initialize();
		
		registerBeanDefinitionParser("provider", new MSBeanDefinitionParser( ProviderBean.class ));
		registerBeanDefinitionParser("consumer", new MSBeanDefinitionParser( ConsumerBean.class ));
		registerBeanDefinitionParser("server", new MSBeanDefinitionParser( ServerBean.class ));
	}
}