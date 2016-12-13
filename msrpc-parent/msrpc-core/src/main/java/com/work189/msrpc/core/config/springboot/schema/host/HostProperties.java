package com.work189.msrpc.core.config.springboot.schema.host;

import java.util.Properties;

public class HostProperties {

	public final static String HOST_KEY="host.key";
	public final static String HOST_ADDR="host.address";
	public final static String REGISTRY_ADDR="registry.address";
	
	protected Properties properties;
	
	public HostProperties(){
		
	}

	public String getHostIp(){
		String addr = properties.getProperty(HOST_ADDR);
		if(addr == null || addr.isEmpty() || "auto".equals(addr) || addr.indexOf(":")<1){
			addr = new RuntimeProperties().getDefaultHostAddress();
		}
		return addr.split(":")[0];
	}

	public int getHostPort(){
		String addr = properties.getProperty(HOST_ADDR);
		if(addr == null || addr.isEmpty() || "auto".equals(addr) || addr.indexOf(":")<1){
			addr = new RuntimeProperties().getDefaultHostAddress();
		}
		int port = Integer.parseInt( addr.split(":")[1] );
		return port;
	}


}
