package com.work189.msrpc.core.zzdemo.aa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.work189.msrpc.core.container.initialize.RunEnvironment;
import com.work189.msrpc.core.registry.center.RegistryServer;

public class DemoTestAA {

	@SuppressWarnings({ "unused", "resource" })
	public static void mainTest(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		/*if(true){
			new DemoTest().test();
		}*/
		
		//RedisUtil.startMoreThread();
		//TestDB.testStart();
		
		String ip="127.0.0.1";
		ip = args[2];

		String user = args[0];
		int count = Integer.parseInt(args[1]);
		
		RunEnvironment.initialize();
		
		if("server".equals(user)){
			ApplicationContext ac = new ClassPathXmlApplicationContext("conf/applicationContext-server.xml");

			DemoServer server = new DemoServer();
			
			System.out.println("服务端启动成功");
			while(true){
				Thread.sleep(1*100);
			}
		}

		if("client".equals(user)){

			//加载spring配置
			ApplicationContext ac = new ClassPathXmlApplicationContext("conf/applicationContext-client.xml");
			System.out.println("客户端启动成功");
			
			RegistryServer registryServer = new RegistryServer();
			registryServer.start();

			List<DemoClientThread> clientThreads = new ArrayList<>();
			for(int i=0; i<count; i++){
				DemoClientThread ct = new DemoClientThread(ac);
				ct.start();
				clientThreads.add(ct);
			}
			
			System.out.println("线程数量:"+clientThreads.size());
			while(true){
				Thread.sleep(1*100);
			}
		}
	}

}
