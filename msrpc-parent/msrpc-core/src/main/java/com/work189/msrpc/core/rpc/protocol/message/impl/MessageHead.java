package com.work189.msrpc.core.rpc.protocol.message.impl;

public class MessageHead {
	private String version;
	private String charset;
	private String requestId;
	private String requestTime;
	private String appId;
	private String hostNode;
	private String hostIp;
	private String hostSecret;
	private String serviceId;
	private String serviceSecurityKey;
	private String serviceSecurityMode;
	private String serviceBufferData;

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getHostNode() {
		return hostNode;
	}
	public void setHostNode(String hostNode) {
		this.hostNode = hostNode;
	}
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	public String getHostSecret() {
		return hostSecret;
	}
	public void setHostSecret(String hostSecret) {
		this.hostSecret = hostSecret;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceSecurityKey() {
		return serviceSecurityKey;
	}
	public void setServiceSecurityKey(String serviceSecurityKey) {
		this.serviceSecurityKey = serviceSecurityKey;
	}
	public String getServiceSecurityMode() {
		return serviceSecurityMode;
	}
	public void setServiceSecurityMode(String serviceSecurityMode) {
		this.serviceSecurityMode = serviceSecurityMode;
	}
	public String getServiceBufferData() {
		return serviceBufferData;
	}
	public void setServiceBufferData(String serviceBufferData) {
		this.serviceBufferData = serviceBufferData;
	}
}
