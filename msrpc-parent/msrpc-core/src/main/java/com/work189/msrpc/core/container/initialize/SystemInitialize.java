package com.work189.msrpc.core.container.initialize;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import com.work189.msrpc.core.config.properties.PropertiesLoader;
import com.work189.msrpc.core.config.properties.property.PropertyMsrpcDefine;
import com.work189.msrpc.core.container.tomcat.TomcatUtil;
import com.work189.msrpc.core.registry.center.RegistryServerManager;
import com.work189.msrpc.core.rpc.exchange.ExchangeManager;
import com.work189.msrpc.core.rpc.exchange.host.DefaultExchangeHostConfig;
import com.work189.msrpc.core.transport.channel.worker.WorkerDispatch;

public class SystemInitialize {
	public static boolean runing = false;
	private static boolean initializeFlag = false;
	
	public SystemInitialize(){
		initialize();
	}
	
	public static synchronized void initialize(){
		if(initializeFlag){
			//已初始成功,不得重复初始化
			return ;
		}
		runing = true;
		initializeFlag = true;
		
		System.out.println("system initialize " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		
		//系统配置
		PropertiesLoader.load();
		
		//初始系统运行环境
		RunEnvironment.initialize();
		
		//服务注册中心
		RegistryServerManager.initialize();

		//jdbc 驱动
		try {
			Class.forName( PropertyMsrpcDefine.msrpc_registry_driver.toString() ).newInstance();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		//JVM系统停止
		Runtime.getRuntime().addShutdownHook( new Thread(){
			public void run(){
				try{
					System.out.println("服务器停止"+DefaultExchangeHostConfig.config.getHostKey());
					RegistryServerManager.clearAndExit();
					//SpringbootInitializeBean.close();
				}catch(Throwable e){
					
				}
			}
		});
		
	}

	public static synchronized void stop(){
		
		if(runing == false){
			return;
		}
		runing = false;
		
		
		try {
			RegistryServerManager.destroy();
			ExchangeManager.transporter.destroy();
			WorkerDispatch.destroy();

			//尝试停止tomcat
			TomcatUtil.stopTomcat();
			
			AbandonedConnectionCleanupThread.shutdown();
			//java.sql.Driver jdbcDriver = DriverManager.getDriver(PropertyMsrpcDefine.msrpc_registry_url.toString());
			//DriverManager.deregisterDriver(jdbcDriver);
			//DriverManager.deregisterDriver( DriverManager.getDrivers().nextElement() );
		} catch (Throwable e) {
			e.printStackTrace();
		}


		System.out.println("SystemInitialize application service stoped");
	}
	
	public static void killThread(){
		
		System.out.println("printThread2----------------------------------------------------------------------------------------------------");
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		ThreadGroup topGroup = group;
		while (group != null) {  
		    topGroup = group;  
		    group = group.getParent();  
		}
		
		int estimatedSize = topGroup.activeCount() * 2;  
		Thread[] slackList = new Thread[estimatedSize];
		int actualSize = topGroup.enumerate(slackList);
		
		Thread[] list = new Thread[actualSize];  
		System.arraycopy(slackList, 0, list, 0, actualSize);  
		System.out.println("Thread list size == " + list.length);  
		for (Thread thread : list) {  
		    System.out.println(thread.getName()+"=="+thread.isDaemon()+";"+thread.isAlive()+";"+thread.isInterrupted());
		    //thread.setDaemon(true);
		    if(!thread.isDaemon() && thread.isAlive()){
		    	//thread.interrupt();
		    	try{
		    		thread.setDaemon(true);
		    	}catch(Throwable e){
		    		
		    	}
		    }
		}
		
	}
	
	public static void printThread(){
		System.out.println("printThread2----------------------------------------------------------------------------------------------------");
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		ThreadGroup topGroup = group;
		while (group != null) {  
		    topGroup = group;  
		    group = group.getParent();  
		}
		
		int estimatedSize = topGroup.activeCount() * 2;  
		Thread[] slackList = new Thread[estimatedSize];
		int actualSize = topGroup.enumerate(slackList);
		
		Thread[] list = new Thread[actualSize];  
		System.arraycopy(slackList, 0, list, 0, actualSize);  
		System.out.println("Thread list size == " + list.length);  
		for (Thread thread : list) {  
		    System.out.println(thread.getName()+"=="+thread.isDaemon()+";"+thread.isAlive()+";"+thread.isInterrupted());
		    //thread.setDaemon(true);
		    if(!thread.isDaemon() && thread.isAlive()){
		    	//thread.interrupt();
		    }
		}
		
	}
}
