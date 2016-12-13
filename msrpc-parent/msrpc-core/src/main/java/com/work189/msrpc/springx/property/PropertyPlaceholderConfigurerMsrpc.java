package com.work189.msrpc.springx.property;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.StringUtils;

import com.work189.msrpc.security.DESUtils;


public class PropertyPlaceholderConfigurerMsrpc extends PropertyPlaceholderConfigurer{
	private String securityFile;
	
	private String jdbcDirFile = "";
	private StringBuilder encryptFields = new StringBuilder();
	private final static String EDAS_FILE="spring.edas.config";
	
	private ConfigurableListableBeanFactory configurableListableBeanFactory;
	protected static Map<String, String> g_SpringPropertiesMap = new HashMap<String, String>();

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		this.configurableListableBeanFactory = beanFactoryToProcess;
		
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("run="+System.getProperty("spring.profiles.active"));
		
		jdbcDirFile = props.getProperty( EDAS_FILE );
		System.out.println("os.name="+System.getProperties().getProperty("os.name")+";jdbcDirFile="+jdbcDirFile);
		this.securityFile = jdbcDirFile;

		//远程文件
		Properties securityFile = getSecurityFileToProperties();
		if(securityFile != null){
			for(Entry<Object, Object> field:securityFile.entrySet()){
				props.setProperty( (String)field.getKey(), (String)field.getValue() );
				props.put(field.getKey(), field.getValue());
			}
		}
		super.processProperties(beanFactoryToProcess, props);
		
		
		//敏感字段不打印----------------------------------------
		encryptFields.append(",jdbc.password,");
		for(Entry<Object, Object> field:props.entrySet()){
			String key = (String) field.getKey();
			String value = (String) field.getValue();
			
			if(true && this.encryptFields.toString().indexOf(key)>0){
				System.out.println(key+"="+"************");	
			}else{
				System.out.println(key+"="+value);
			}
			
			value = beanFactoryToProcess.resolveEmbeddedValue((String)field.getValue());
			g_SpringPropertiesMap.put(key, value);
		}
		System.out.println("---------------------------------------------------------------------------------");

	}

	public Properties getSecurityFileToProperties(){
		String strSecurityFile = null;
		if(StringUtils.isEmpty(strSecurityFile)){
			strSecurityFile = System.getProperty( EDAS_FILE );
			System.out.println("检查JVM环境输入securityFile="+strSecurityFile);
		}
		if(StringUtils.isEmpty(strSecurityFile)){
			strSecurityFile = System.getenv( EDAS_FILE );
			System.out.println("检查系统环境输入securityFile="+strSecurityFile);
		}
		if(StringUtils.isEmpty(strSecurityFile)){
			strSecurityFile = this.securityFile;
		}
		
		if(!StringUtils.isEmpty(strSecurityFile)){
			return getSecurityFileToProperties(strSecurityFile);
		}
		return null;
	}
	
	public Properties getSecurityFileToProperties(String filename){
		
		//文件名格式化处理
		filename = configurableListableBeanFactory.resolveEmbeddedValue(filename);

		System.out.println("读取本地加密文件V1.0:"+filename);
		if(!(new File(filename).canRead())){
			System.out.println("配置文件不可读取:"+filename);
			return null;
		}
		try{
			Properties properties = new Properties();
			properties.load( new FileInputStream(filename) );

			String lable="encrypt.";
			for(Entry<Object, Object> field:properties.entrySet()){
				String key = (String) field.getKey();
				String value = (String) field.getValue();
				
				if(!key.startsWith(lable)){
					//非加密字段
					continue;
				}
				key = key.substring(lable.length());
				
				if(!StringUtils.isEmpty(value) && !StringUtils.isEmpty(key) && value.length() >= 8){
					value = DESUtils.decrypt("9vYiwpYmIpvUQrmZyGLaiK1NOZHRGq0Z", value);//解密
					properties.setProperty( key, value );
					this.encryptFields.append(key+",");
				}
			}
			return properties;
			
		}catch(Throwable e){
			e.printStackTrace();
		}
		return null;
	}


	public String getSecurityFile() {
		return securityFile;
	}

	public void setSecurityFile(String securityFile) {
		this.securityFile = securityFile;
	}
	
	public static String getPropertiesValue(String name){
		return PropertyPlaceholderConfigurerMsrpc.g_SpringPropertiesMap.get(name);
	}
	
}
