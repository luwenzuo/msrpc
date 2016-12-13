package com.work189.msrpc.core.config.springboot.schema.entity;

import java.io.Serializable;

public class ServerEntity<T> implements Serializable {
	private static final long serialVersionUID = -109943770723888870L;
	private long wkTimeout;
	private String wkId;
	private String wkRef;
	private String wkGroup;
	private String wkVersion;
	private String wkInterface;
	
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
	public String getWkRef() {
		return wkRef;
	}
	public void setWkRef(String wkRef) {
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
	}
	
	
}
