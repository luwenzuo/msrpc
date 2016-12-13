package com.work189.msrpc.core.common.system;

public class MSOperatingSystem {

	public String getSystemName(){
		return System.getProperties().getProperty("os.name");
	}
	
	public static boolean isUnixOS(){
		return false;
	}
	
	public static boolean isWindowsOS(){
		return false;
	}
}
