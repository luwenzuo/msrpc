package com.work189.msrpc.core.registry.center;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.work189.msrpc.core.container.initialize.SystemInitialize;

public class RegistryServer {
	public boolean running = true;
	private static RegistryWorker registryWorker;

	public synchronized void start(  ){
		if(registryWorker == null){
			
			//监控
			registryWorker = new RegistryWorker();
			registryWorker.setDaemon(true);
			registryWorker.start();
		}
	}
	public synchronized void stop(  ){
		if(registryWorker != null){
			running = false;
			registryWorker.interrupt();
		}
	}
	
	private class RegistryWorker extends Thread{
		
		@Override
		public void run(){
			
			while(running){
				try{
					if(SystemInitialize.runing == false){
						Thread.sleep(1*1000);
						continue;
					}
					RegistryServer.monitor();
				}catch(InterruptedException e){
					
				}catch(Throwable e){
					e.printStackTrace();
				}
			}
		}
	}

	private static Lock lock = new ReentrantLock();// 锁对象
	private static Condition condition = lock.newCondition();
	private static void monitor() throws Exception {

		try{
			lock.lock();
			if(condition.await(10 * 1000, TimeUnit.MILLISECONDS) == false){
			}

			// 检查consumer
			RegistryCheckService.checkAllConsumerServices();
	
			// 检查provider
			RegistryCheckService.checkAllProviderServices();
			
			//检查服务器
			RegistryCheckHost.activeProviderHost();
		}finally{
			lock.unlock();
		}

	}

	public static void checkService() {
		lock.lock();
		condition.signal();
		lock.unlock();
	}

}
