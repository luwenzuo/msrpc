package com.work189.msrpc.core.rpc.exchange.buffer;

public class ExchangeRequest {

	// 接口编号
	private String serviceId;
	// 接口
	private String serviceName;
	// 方法
	private String methodName;
	// 输入参数
	private Class<?>[] inputArgsType;
	private Object[] inputArgsValues;
	// 服务器信息
	private String hostIp;
	private int hostPort;

	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Class<?>[] getInputArgsType() {
		return inputArgsType;
	}
	public void setInputArgsType(Class<?>[] inputArgsType) {
		this.inputArgsType = inputArgsType;
	}
	public Object[] getInputArgsValues() {
		return inputArgsValues;
	}
	public void setInputArgsValues(Object[] inputArgsValues) {
		this.inputArgsValues = inputArgsValues;
	}
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	public int getHostPort() {
		return hostPort;
	}
	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}
}
