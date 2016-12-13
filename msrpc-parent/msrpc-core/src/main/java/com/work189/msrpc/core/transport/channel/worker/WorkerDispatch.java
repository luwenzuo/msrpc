package com.work189.msrpc.core.transport.channel.worker;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.work189.msrpc.core.config.properties.property.PropertyMsrpcDefine;
import com.work189.msrpc.core.transport.channel.Channel;

public class WorkerDispatch{
	
	private static ThreadPoolExecutor threadPoolWorkers;
	
	static{
		init();
	}

	private static void init(){
		LinkedBlockingQueue<Runnable> taskQueue = 
				new LinkedBlockingQueue<Runnable>(PropertyMsrpcDefine.msrpc_server_queue.toInteger());
		
		threadPoolWorkers = new ThreadPoolExecutor(
				PropertyMsrpcDefine.msrpc_server_init.toInteger(),
				PropertyMsrpcDefine.msrpc_server_max.toInteger(), 
				PropertyMsrpcDefine.msrpc_server_await.toInteger(), 
				TimeUnit.MILLISECONDS, taskQueue, 
				new CustomRejectedExecutionHandler());

	}
	
	public static void addTask(Channel channel, Object message){
		threadPoolWorkers.execute( new WorkerTask(channel, message) );
		/*System.out.println(
				threadPoolWorkers.getActiveCount()+";"+
				threadPoolWorkers.getTaskCount()+";"+
				threadPoolWorkers.getQueue().size() );*/
	}

	private static class CustomRejectedExecutionHandler implements RejectedExecutionHandler{

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			System.out.println("任务超限");
			System.out.println(
			threadPoolWorkers.getActiveCount()+";"+
			threadPoolWorkers.getPoolSize()+";"+
			threadPoolWorkers.getTaskCount()+";"+
			threadPoolWorkers.getQueue().size() );
		}
		
	}

	public static void destroy() {
		if(threadPoolWorkers!=null){
			threadPoolWorkers.shutdownNow();
		}
	}
}
