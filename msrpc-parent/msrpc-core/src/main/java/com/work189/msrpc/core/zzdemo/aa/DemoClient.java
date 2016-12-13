package com.work189.msrpc.core.zzdemo.aa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.work189.msrpc.core.registry.center.RegistryServer;

public class DemoClient {
	protected static ApplicationContext ac = null;

	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub

		//加载spring配置
		ac = new ClassPathXmlApplicationContext("conf/applicationContext-client.xml");
		System.out.println("客户端启动成功");
		
		List<DemoClientThread> clientThreads = new ArrayList<>();
		int count = Integer.parseInt(args[0]);
		for(int i=0; i<count; i++){
			DemoClientThread ct = new DemoClientThread(ac);
			ct.start();
			clientThreads.add(ct);
		}
		
		RegistryServer registryServer = new RegistryServer();
		registryServer.start();
		
		Thread.sleep(2*1000);
		System.out.println("线程数量:"+clientThreads.size());
		while(true){
			Thread.sleep(1*100);
		}
	}
}
