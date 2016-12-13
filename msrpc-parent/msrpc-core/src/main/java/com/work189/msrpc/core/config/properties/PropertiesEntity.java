package com.work189.msrpc.core.config.properties;

import java.util.Properties;

public class PropertiesEntity {

	private Properties properties;

	public int getInt(String key){
		return Integer.parseInt(this.getString(key));
	}
	
	public String getString(String key){
		return properties.getProperty(key);
	}
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	
}
