package com.work189.msrpc.core.rpc.exchange.buffer.rpcbuffer;

import java.nio.ByteBuffer;

public class RpcBufferAllocate extends RpcBuffer{

	protected ByteBuffer buf = null;
	
	public RpcBuffer allocateBuffer(int capacity){
		buf = ByteBuffer.allocate(capacity);
		return this;
	}
	
	public ByteBuffer getByteBuffer(){
		return this.buf;
	}
	
	public void put(byte[] data) {
		if(data == null){
			return;
		}
		if(buf == null){
			allocate(data.length);
		}
		
		buf.put( data );
	}

	public byte[] array() {
		if(buf != null){
			return buf.array();
		}
		return null;
	}

}
