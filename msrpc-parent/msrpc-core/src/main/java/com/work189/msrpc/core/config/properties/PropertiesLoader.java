package com.work189.msrpc.core.config.properties;

import com.work189.msrpc.core.config.properties.PropertiesFactory;
import com.work189.msrpc.core.config.properties.property.PropertyMsrpcDefine;

public class PropertiesLoader {

	public static void load(){
		
		//系统公共配置文件
		PropertiesFactory.addPropertiesEntity(PropertyMsrpcDefine.class, 
				PropertiesLoader.class.getClassLoader().getResourceAsStream("msrpc.properties"));
	}
}
