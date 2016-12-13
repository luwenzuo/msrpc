package com.work189.msrpc.core.common.system;

public class MSEnvironment {

	public String getEnvName(){
		return System.getProperty("spring.profiles.active");
	}
}
