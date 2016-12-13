package com.work189.msrpc.core.transport.channel.swap;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.work189.msrpc.core.config.properties.property.PropertyMsrpcDefine;

@SuppressWarnings("unused")
public class ChannelFuture {

	private Lock lock = new ReentrantLock();//锁对象 
	private Condition condition = lock.newCondition();
	private Object result=null;
	private boolean ok=false;
	private long beginTime = 0;
	private long timeout = 0;
	public int requestId = 0;
	public String hostKey;

	public static ChannelFuture newFuture(){
		ChannelFuture future = new ChannelFuture();
		future.ok = false;
		future.timeout = PropertyMsrpcDefine.msrpc_client_await.toLong();
		future.beginTime = System.currentTimeMillis();
		future.requestId = ChannelQueue.queue.getRequestId();
		ChannelQueue.queue.add(future.requestId, future);
		return future;
	}
	
	public Object get() {
		try {
			long beginTime = System.currentTimeMillis();
			lock.lock();
			while(!isOk()){
				if(condition.await(1000, TimeUnit.MILLISECONDS) == false){
					//System.out.println("timeout="+this.requestId+";"+isOk()+";"+System.currentTimeMillis()+";"+ ChannelQueue.queueMap.size());
				}
				if(isOk() || (System.currentTimeMillis()-beginTime > 10*1000) ){
					break;
				}
			}
			if(!isOk()){
				throw new RuntimeException("客户端超时");
			}
			result = ChannelQueue.queue.getAndRemove(this.requestId);
			if(result == null){
				throw new RuntimeException("通讯故障");
			}
			
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("requestId="+this.requestId);
			ChannelQueue.queue.remove(requestId);
			throw new RuntimeException(e.getMessage());
		}finally{
			lock.unlock();
		}
	}
	
	public void done(){
		this.ok = true;
		lock.lock();
		condition.signal();
		lock.unlock();
	}

	public boolean isOk() {
		return ok;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
