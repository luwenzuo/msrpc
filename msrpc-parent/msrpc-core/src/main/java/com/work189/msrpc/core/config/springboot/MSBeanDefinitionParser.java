package com.work189.msrpc.core.config.springboot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import com.work189.msrpc.core.config.springboot.schema.ConsumerBean;
import com.work189.msrpc.core.config.springboot.schema.ProviderBean;
import com.work189.msrpc.core.config.springboot.schema.ServerBean;

public class MSBeanDefinitionParser implements BeanDefinitionParser {
	static final Map<String, FieldDefenition> fieldDefMap = new HashMap<String, FieldDefenition>();
	@SuppressWarnings("rawtypes")
	private final Class clazz;

	@SuppressWarnings("rawtypes")
	public MSBeanDefinitionParser(Class clazz) {
		this.clazz = clazz;
	}

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		if (this.clazz == ProviderBean.class){
			return parseProvider(element, parserContext);
		}
		if (this.clazz == ConsumerBean.class) {
			return parseConsumer(element, parserContext);
		}
		if (this.clazz == ServerBean.class) {
			return parseServer(element, parserContext);
		}
		throw new BeanDefinitionValidationException("Unknown class to definition " + this.clazz.getName());
	}

	public BeanDefinition parseProvider(Element element,
			ParserContext parserContext) {
		RootBeanDefinition beanDef = new RootBeanDefinition();
		beanDef.setBeanClass(this.clazz);
		beanDef.setLazyInit(false);// 延时加载
	    
	    //解析字段属性
	    parseAttribute(element, beanDef);

		String id = getId( parserContext, element.getAttribute("id") );
		BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDef, id);
		BeanDefinitionReaderUtils.registerBeanDefinition(holder,parserContext.getRegistry());
		return beanDef;
	}

	public BeanDefinition parseConsumer(Element element,
			ParserContext parserContext) {
		RootBeanDefinition beanDef = new RootBeanDefinition();
		beanDef.setBeanClass(this.clazz);
		beanDef.setLazyInit(false);
		
		//解析字段属性
		parseAttribute(element, beanDef);
		
		String id = getId(parserContext, element.getAttribute("id"));
		BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDef, id);
		BeanDefinitionReaderUtils.registerBeanDefinition(holder,parserContext.getRegistry());
		return beanDef;
	}
	


	public BeanDefinition parseServer(Element element,
			ParserContext parserContext) {
		RootBeanDefinition beanDef = new RootBeanDefinition();
		beanDef.setBeanClass(this.clazz);
		beanDef.setLazyInit(false);
		
		//解析字段属性
		parseAttribute(element, beanDef);

		String id = getId(parserContext, element.getAttribute("id"));
		BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDef, id);
		BeanDefinitionReaderUtils.registerBeanDefinition(holder,parserContext.getRegistry());
		return beanDef;
	}

	private void checkAttr(String attrName, String attr) {
		if ((null == attr) || (attr.isEmpty()))
			throw new RuntimeException("attribute " + attrName + " must set.");
	}

	private String getId(ParserContext parserContext, String id) {
		if ((id == null) || (id.length() == 0)) {
			id = this.clazz.getSimpleName();
		}
		int time = 3;
		while ((parserContext.getRegistry().containsBeanDefinition(id))
				&& (time > 0)) {
			id = id + time;
			time--;
		}
		if (parserContext.getRegistry().containsBeanDefinition(id)) {
			throw new IllegalStateException("Duplicate spring bean id " + id);
		}
		return id;
	}

	private void parseAttribute(Element element, RootBeanDefinition beanDef) {
		NamedNodeMap attrMap = element.getAttributes();
		for (int i = 0; i < attrMap.getLength(); i++) {
			Attr attr = (Attr) attrMap.item(i);
			String name = attr.getName();
			String value = attr.getValue();
			if ((null != name) && (!name.isEmpty()) && 
				(null != value)&& (!value.isEmpty()) ){
				
				//引用类
				if("ref".equals(name)){
					String targetRef = element.getAttribute("ref");
					checkAttr("ref", targetRef);
				    beanDef.getPropertyValues().addPropertyValue("wkRef", new RuntimeBeanReference(targetRef));
				    continue;
				}
			    
				FieldDefenition field = fieldDefMap.get(name);
				if (field != null && field.getPropName() != null) {
					beanDef.getPropertyValues().add(field.getPropName(), value);
				} else {
					//beanDef.getPropertyValues().add(name, value);
				}
			}
		}
	}

	static {
		fieldDefMap.put("interface", new FieldDefenition("wkInterface", true, false));
		fieldDefMap.put("timeout", new FieldDefenition("wkTimeout",false, false));
		fieldDefMap.put("version", new FieldDefenition("wkVersion", false, false));
		fieldDefMap.put("group", new FieldDefenition("wkGroup",false, false));
		fieldDefMap.put("ref", new FieldDefenition("wkRef",false, false));
		fieldDefMap.put("id", new FieldDefenition("wkId",false, false));
	}

	static class FieldDefenition {
		String propName;
		boolean requred;
		final boolean discard;

		public FieldDefenition(String propName, boolean requred, boolean discard) {
			this.propName = propName;
			this.requred = requred;
			this.discard = discard;
		}

		public FieldDefenition(boolean discard) {
			this.discard = discard;
		}

		public String getPropName() {
			return this.propName;
		}

		public void setPropName(String propName) {
			this.propName = propName;
		}

		public boolean isRequred() {
			return this.requred;
		}

		public void setRequred(boolean requred) {
			this.requred = requred;
		}

		public boolean isDiscard() {
			return this.discard;
		}
	}
}