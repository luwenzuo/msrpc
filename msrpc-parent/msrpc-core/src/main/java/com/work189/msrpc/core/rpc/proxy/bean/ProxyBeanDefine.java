package com.work189.msrpc.core.rpc.proxy.bean;

public class ProxyBeanDefine {

	private Object object;
	private Class<?> interfaceClass;
	private String interfaceName;
	private String serviceId;
	private String hostKey;
	private String hostIp;
	private int hostPort;
	private String activeKey;
	private String group;
	private String version;
	private String serviceHostKey;
	private String serviceHostIp;
	private int serviceHostPort;
	private String pcFlag;
	private boolean ok = false;
	
	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Class<?> getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getServiceId() {
		if(this.serviceId == null){
			this.serviceId = this.getGroup()+":"+this.getInterfaceName()+":"+this.getVersion();
		}
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceHostKey() {
		return serviceHostKey;
	}

	public void setServiceHostKey(String serviceHostKey) {
		this.serviceHostKey = serviceHostKey;
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

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHostKey() {
		this.hostKey = this.getHostIp()+":"+this.getHostPort();
		return hostKey;
	}

	public void setHostKey(String hostKey) {
		this.hostKey = hostKey;
	}

	public String getServiceHostIp() {
		return serviceHostIp;
	}

	public void setServiceHostIp(String serviceHostIp) {
		this.serviceHostIp = serviceHostIp;
	}

	public int getServiceHostPort() {
		return serviceHostPort;
	}

	public void setServiceHostPort(int serviceHostPort) {
		this.serviceHostPort = serviceHostPort;
	}

	public String getPcFlag() {
		return pcFlag;
	}

	public void setPcFlag(String pcFlag) {
		this.pcFlag = pcFlag;
	}

	public String getActiveKey() {
		return activeKey;
	}

	public void setActiveKey(String activeKey) {
		this.activeKey = activeKey;
	}
	
	
}
