/*
 * Copyright 2015 dactiv
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.work189.msrpc.springx.property;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.work189.msrpc.security.DESUtils;


/**
 * @author
 */
public class PropertySourcesPlaceholderConfigurerMsrpc extends
		PropertySourcesPlaceholderConfigurer {
	private String securityFile;
	private StringBuilder encryptFields = new StringBuilder();
	final static String EDAS_FILE = "spring.edas.config";
	private ConfigurableListableBeanFactory configurableListableBeanFactory;

	@SuppressWarnings("unchecked")
	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		this.configurableListableBeanFactory = beanFactory;

		try {
			System.out.println("---------------------------------------------------------------------------------");
			System.out.println("运行环境run="+ System.getProperty("spring.profiles.active"));
			System.out.println("os.name="+ System.getProperties().getProperty("os.name"));

			// 远程文件
			Properties securityFileProp = this.getSecurityFileToProperties();
			if (securityFileProp != null) {
				{//1:优先初化远程配置文件
					MutablePropertySources propertySource = new MutablePropertySources();
					PropertySource<?> newPropertySource = new PropertiesPropertySource( LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME, securityFileProp);
					if (this.localOverride) {
						propertySource.addFirst(newPropertySource);
					} else {
						propertySource.addLast(newPropertySource);
					}
					super.processProperties(beanFactory, new PropertySourcesPropertyResolver(propertySource));
					super.postProcessBeanFactory(beanFactory);
				}
				{//2:全局重构配置文件
					PropertySources allPropertySources = this.getAppliedPropertySources();
					Properties localMap = mergeProperties();
					CollectionUtils.mergePropertiesIntoMap(securityFileProp, localMap);
					MutablePropertySources propertySource = new MutablePropertySources( allPropertySources);
					PropertySource<?> newPropertySource = new PropertiesPropertySource( LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME, localMap);
					if (this.localOverride) {
						propertySource.addFirst(newPropertySource);
					} else {
						propertySource.addLast(newPropertySource);
					}
					this.setPropertySources(propertySource);
					super.postProcessBeanFactory(beanFactory);
				}
			}

			super.postProcessBeanFactory(beanFactory);


			// 敏感字段不打印----------------------------------------
			encryptFields.append(",jdbc.password,");
			PropertySources allPropertySources = this.getAppliedPropertySources();
			PropertySource<?> localPropertySource = allPropertySources.get(LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME);
			Map<String, String> localMap = (Map<String, String>) localPropertySource
					.getSource();
			for (Entry<String, String> field : localMap.entrySet()) {
				String key = (String) field.getKey();
				String value = (String) field.getValue();

				if (true && this.encryptFields.toString().indexOf(key) > 0) {
					System.out.println(key + "=" + "************");
				} else {
					System.out.println(key + "=" + value);
					// System.out.println(key+"="+beanFactory.resolveEmbeddedValue(value));
				}
				value = beanFactory.resolveEmbeddedValue(value);
				PropertyPlaceholderConfigurerMsrpc.g_SpringPropertiesMap.put(key,
						value);
			}
			System.out.println("---------------------------------------------------------------------------------");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Properties getSecurityFileToProperties() {
		String strSecurityFile = null;
		if (StringUtils.isEmpty(strSecurityFile)) {
			strSecurityFile = System.getProperty(EDAS_FILE);
			System.out.println("检查JVM环境输入securityFile=" + strSecurityFile);
		}
		if (StringUtils.isEmpty(strSecurityFile)) {
			strSecurityFile = System.getenv(EDAS_FILE);
			System.out.println("检查系统环境输入securityFile=" + strSecurityFile);
		}
		if (StringUtils.isEmpty(strSecurityFile)) {
			strSecurityFile = this.securityFile;
		}

		if (!StringUtils.isEmpty(strSecurityFile)) {
			return getSecurityFileToProperties(strSecurityFile);
		}
		return null;
	}

	private Properties getSecurityFileToProperties(String filename) {

		// 文件名格式化处理
		filename = configurableListableBeanFactory
				.resolveEmbeddedValue(filename);

		System.out.println("读取本地加密文件V1.0:" + filename);
		if (!(new File(filename).canRead())) {
			System.out.println("配置文件不可读取:" + filename);
			return null;
		}
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(filename));

			String lable = "encrypt.";
			for (Entry<Object, Object> field : properties.entrySet()) {
				String key = (String) field.getKey();
				String value = (String) field.getValue();

				if (!key.startsWith(lable)) {
					// 非加密字段
					continue;
				}
				key = key.substring(lable.length());

				if (!StringUtils.isEmpty(value) && !StringUtils.isEmpty(key)
						&& value.length() >= 8) {
					value = DESUtils.decrypt(
							"9vYiwpYmIpvUQrmZyGLaiK1NOZHRGq0Z", value);// 解密
					properties.setProperty(key, value);
					this.encryptFields.append(key + ",");
				}
			}
			return properties;

		} catch (Throwable e) {
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

	public static String getPropertiesValue(String name) {
		return PropertyPlaceholderConfigurerMsrpc.g_SpringPropertiesMap.get(name);
	}
}
