package com.work189.msrpc.core.container.tomcat;

import java.lang.reflect.Method;

import com.work189.msrpc.core.common.system.jvm.ReflectUtil;

public class TomcatUtil {

	private Object catalinaDaemon = null;
	private static Thread tomcatStopThread = null;
	
	public synchronized static void stopTomcat(){
		if(tomcatStopThread != null){
			return;
		}
		tomcatStopThread = new Thread(){
			public void run(){
				while(true){
					try {
						String strStatus = TomcatUtil.getRunStatus();
						if(strStatus.indexOf("_destroy") > 0){
							System.exit(0);
						}
						Thread.sleep(1000);
					} catch (Throwable e) {
						break;
					}
				}
			}
		};
		tomcatStopThread.setDaemon(true);
		tomcatStopThread.start();
	}
	
	public static String getRunStatus(){
		try{
			TomcatUtil tu = new TomcatUtil();
			tu.initTomcat();
			return tu.getStatus();
		}catch(Throwable e){
			throw new RuntimeException(e.getMessage());
		}
	}

	private void initTomcat() {

		// org.apache.catalina.startup.Catalina
		String bootstrapClass = "org.apache.catalina.startup.Bootstrap";
		try {
			Class<?> clazz = Class.forName(bootstrapClass);
			Object classObject = clazz.newInstance();
			Object bootstrap = ReflectUtil.getFieldObject(classObject, "daemon");
			catalinaDaemon = ReflectUtil.getFieldObject(bootstrap,
					"catalinaDaemon");
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}

	private Object getServer() throws Exception {
		String methodName = "getServer";
		Method method = this.catalinaDaemon.getClass().getMethod(methodName,new Class[0]);

		return method.invoke(this.catalinaDaemon, new Object[0]);
	}
	
	private String getStatus() throws Exception {
		String strStatus="";

		String methodName = "getState";
		Object server = this.getServer();
		Method method = server.getClass().getMethod(methodName,new Class[0]);
		Object status = method.invoke(server, new Object[0]);

		method = status.getClass().getMethod("getLifecycleEvent", new Class[0]);
		strStatus += method.invoke(status, new Object[0]);
		
		strStatus += "=";
		method = status.getClass().getMethod("isAvailable", new Class[0]);
		strStatus += method.invoke(status, new Object[0]);
		
		return strStatus;
	}

	/*
	 * NEW(false, null), 
	  INITIALIZING(false, "before_init"), 
	  INITIALIZED(false, "after_init"), 
	  STARTING_PREP(false, "before_start"), 
	  STARTING(true, "start"), 
	  STARTED(true, "after_start"), 
	  STOPPING_PREP(true, "before_stop"), 
	  STOPPING(false, "stop"), 
	  STOPPED(false, "after_stop"), 
	  DESTROYING(false, "before_destroy"), 
	  DESTROYED(false, "after_destroy"), 
	  FAILED(false, null), 
	  MUST_STOP(true, null), 
	  MUST_DESTROY(false, null);
	 * */
}
