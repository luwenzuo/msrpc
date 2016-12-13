package com.work189.msrpc.core.config.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesFactory {
	private static Map<String, PropertiesEntity> propertiesFactoryMap = new HashMap<String, PropertiesEntity>();
	
	public static PropertiesEntity getPropertiesEntity(Class<?> clazz){
		return propertiesFactoryMap.get(clazz.getName());
	}
	
	public static PropertiesEntity getPropertiesEntity(String key){
		return propertiesFactoryMap.get(key);
	}

	public static void addPropertiesEntity(Class<?> clazz, String filename) {
		FileInputStream fis = null;
		try {
			File file = new File(filename);
			fis = new FileInputStream(file);
			
			addPropertiesEntity(clazz, fis);
		} catch (Throwable e) {
			throw new RuntimeException("加载配置失败" + filename);
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void addPropertiesEntity(Class<?> clazz, InputStream inputStream) {
		if(inputStream == null){
			return;
		}
		
		FileInputStream fis = null;
		try {
			
			PropertiesEntity propertiesEntity = new PropertiesEntity();
			Properties properties = new Properties();
			properties.load(inputStream);
			propertiesEntity.setProperties( properties );
			addPropertiesEntity(clazz.getCanonicalName(), propertiesEntity);
		} catch (Throwable e) {
			throw new RuntimeException("加载配置失败");
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public static void addPropertiesEntity(Class<?> clazz, PropertiesEntity propertiesEntity){
		addPropertiesEntity(clazz.getCanonicalName(), propertiesEntity);
	}
	
	public static void addPropertiesEntity(String key, PropertiesEntity propertiesEntity){
		propertiesFactoryMap.put(key, propertiesEntity);
	}
}
