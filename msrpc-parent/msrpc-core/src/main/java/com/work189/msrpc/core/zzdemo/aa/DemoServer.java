package com.work189.msrpc.core.zzdemo.aa;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.work189.msrpc.core.registry.center.RegistryServer;

public class DemoServer {
	
	public DemoServer(){

		RegistryServer registryServer = new RegistryServer();
		registryServer.start();
	}
	

	@SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub

		ApplicationContext ac = new ClassPathXmlApplicationContext("conf/applicationContext-server.xml");

		DemoServer server = new DemoServer();
		
		RegistryServer registryServer = new RegistryServer();
		registryServer.start();
		
		System.out.println("服务端启动成功");
		while(true){
			Thread.sleep(1*100);
		}
	}
}
