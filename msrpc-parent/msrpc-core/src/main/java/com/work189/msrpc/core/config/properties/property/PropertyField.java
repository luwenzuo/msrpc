package com.work189.msrpc.core.config.properties.property;

import com.work189.msrpc.core.config.properties.PropertiesEntity;
import com.work189.msrpc.core.config.properties.PropertiesFactory;


@SuppressWarnings("rawtypes")
public class PropertyField {
	private String name;
	private Class clazz;
	private String defaultValue;
	
	public PropertyField(String name, Class clazz){
		this.name = name;
		this.clazz = clazz;
	}
	
	public PropertyField(String name, Class clazz, String defaultValue){
		this.name = name;
		this.clazz = clazz;
		this.defaultValue = defaultValue;
	}
	
	public String toString(){
		PropertiesEntity propertiesEntity = PropertiesFactory.getPropertiesEntity(clazz);
		if(propertiesEntity != null){
			String str = propertiesEntity.getString(name);
			if(str == null && defaultValue != null){
				return defaultValue;
			}
			return str;
		}
		return null;
	}
	
	public Integer toInteger(){
		try{
			return Integer.parseInt( toString() );
		}catch(Throwable e){
			return null;
		}
	}
	
	public Long toLong(){
		try{
			return Long.parseLong( toString() );
		}catch(Throwable e){
			return null;
		}
	}
	
	public Double toBigDecimal(){
		try{
			return Double.parseDouble( toString() );
		}catch(Throwable e){
			return null;
		}
	}
}
