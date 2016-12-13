package com.work189.msrpc.core.config.springboot.schema.entity;

import java.io.Serializable;

import com.work189.msrpc.core.rpc.proxy.bean.ProxyBeanDefine;

public class ProviderEntity<T> implements Serializable {
	private static final long serialVersionUID = -3202911626112296459L;
	protected transient volatile T ref;
	private long wkTimeout;
	private String wkId;
	private T wkRef;
	private String wkGroup;
	private String wkVersion;
	private String wkInterface;
	protected Class<?> interfaceClass;
	protected ProxyBeanDefine proxyBeanDefine = new ProxyBeanDefine();
	
	public long getWkTimeout() {
		return wkTimeout;
	}
	public void setWkTimeout(long wkTimeout) {
		this.wkTimeout = wkTimeout;
	}
	public String getWkId() {
		return wkId;
	}
	public void setWkId(String wkId) {
		this.wkId = wkId;
	}
	public T getWkRef() {
		return wkRef;
	}
	public void setWkRef(T wkRef) {
		this.wkRef = wkRef;
	}
	public String getWkGroup() {
		return wkGroup;
	}
	public void setWkGroup(String wkGroup) {
		this.wkGroup = wkGroup;
	}
	public String getWkVersion() {
		return wkVersion;
	}
	public void setWkVersion(String wkVersion) {
		this.wkVersion = wkVersion;
	}
	public String getWkInterface() {
		return wkInterface;
	}
	public void setWkInterface(String wkInterface) {
		this.wkInterface = wkInterface;
		try {
			this.interfaceClass = Class.forName(this.getWkInterface(), true, Thread.currentThread().getContextClassLoader());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
}
