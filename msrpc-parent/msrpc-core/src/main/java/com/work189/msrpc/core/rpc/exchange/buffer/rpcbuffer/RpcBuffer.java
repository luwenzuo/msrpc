package com.work189.msrpc.core.rpc.exchange.buffer.rpcbuffer;


public abstract class RpcBuffer {
	
	public static RpcBuffer allocate(int capacity){
		return new RpcBufferAllocate().allocateBuffer(capacity);
	}
	public static RpcBuffer wrap(byte[] data){
		RpcBuffer buf = new RpcBufferAllocate().allocateBuffer(data.length);
		buf.put(data);
		return buf;
	}

	public abstract byte[] array();
	public abstract void put(byte[] data);
	
}
