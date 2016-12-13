package com.work189.msrpc.core.rpc.exchange.host;

import java.util.UUID;

import com.work189.msrpc.core.config.springboot.schema.host.HostUtil;

public class DefaultExchangeHostConfig {
	
	public static DefaultExchangeHostConfig config = new DefaultExchangeHostConfig();
	private String hostKey = "127.0.0.1:22109";
	private String hostIp = "127.0.0.1";
	private int hostPort = 22109;
	private String activeKey;
	
	public DefaultExchangeHostConfig(){
		this.activeKey = UUID.randomUUID().toString().replaceAll("-", "");
	}

	public String getHostKey() {
		this.hostKey = this.hostIp+":"+this.hostPort;
		return hostKey;
	}
	public void setHostKey(String hostKey) {
		this.hostKey = hostKey;
	}
	public String getHostIp() {
		this.hostIp = HostUtil.getDefaultHostIp();
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
	public String getActiveKey() {
		return activeKey;
	}
	public void setActiveKey(String activeKey) {
		this.activeKey = activeKey;
	}
}
