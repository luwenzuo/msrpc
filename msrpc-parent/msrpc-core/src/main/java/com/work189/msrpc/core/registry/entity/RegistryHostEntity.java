package com.work189.msrpc.core.registry.entity;

import com.work189.msrpc.core.transport.Server;

public class RegistryHostEntity {
	private String activeKey;
	private String hostKey;
	private String hostIP;
	private int hostPort;
	private Server server;
	
	public String getHostKey() {
		return hostKey;
	}
	public void setHostKey(String hostKey) {
		this.hostKey = hostKey;
	}
	public String getHostIP() {
		return hostIP;
	}
	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
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
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
}
