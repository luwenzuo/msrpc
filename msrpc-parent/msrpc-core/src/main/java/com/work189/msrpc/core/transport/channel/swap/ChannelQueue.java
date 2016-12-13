package com.work189.msrpc.core.transport.channel.swap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.work189.msrpc.core.rpc.exchange.buffer.rpcbuffer.RpcBuffer;

public class ChannelQueue {

	public final static ChannelQueue queue = new ChannelQueue();
	public final static Map<Integer, ChannelFuture> queueMap = new ConcurrentHashMap<>();
	protected final static AtomicInteger requestId = new AtomicInteger(1);

	public int getRequestId() {
		int id = requestId.getAndIncrement();
		if(id > Integer.MAX_VALUE){
			synchronized (requestId) {
				if(requestId.get() > Integer.MAX_VALUE){
					requestId.set(0);
				}
				id = requestId.getAndIncrement();
			}
		}
		return id;
	}

	public void add(int id, ChannelFuture channelFuture) {
		if(queueMap.containsKey(id)){
			throw new RuntimeException("消息已存在");
		}
		queueMap.put(id, channelFuture);
	}

	public void remove(int id) {
		queueMap.remove(id);
	}

	public ChannelFuture get(int id) {
		return queueMap.get(id);
	}
	
	public void returnResponse(int id, Object response){
		ChannelFuture future = get(id);
		if(future != null){
			future.setResult(response);
			future.done();
		}else{
			System.err.println("id is not exist = "+id+";"+((RpcBuffer)response).array().length);
		}
	}

	public Object getAndRemove(int id) {
		ChannelFuture future = get(id);
		if(future != null){
			Object obj = future.getResult();
			remove(id);
			return obj;
		}else{
			return null;
		}
	}

}
