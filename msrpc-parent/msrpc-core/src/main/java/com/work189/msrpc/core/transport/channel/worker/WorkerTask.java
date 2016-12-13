package com.work189.msrpc.core.transport.channel.worker;

import java.io.Serializable;

import com.work189.msrpc.core.rpc.exchange.buffer.rpcbuffer.RpcBuffer;
import com.work189.msrpc.core.rpc.exchange.support.DefaultExchangeServerHander;
import com.work189.msrpc.core.transport.channel.Channel;

public class WorkerTask implements Runnable, Serializable{
	private static final long serialVersionUID = -2269645923945960452L;

	private Object message;
	private Channel channel;
	
	public WorkerTask(Channel channel, Object message){
		this.channel = channel;
		this.message = message;
	}

	@Override
	public void run() {
		DefaultExchangeServerHander hander = new DefaultExchangeServerHander();
		RpcBuffer retBuffer =  hander.callService((RpcBuffer) message);
		channel.send(retBuffer, true);
	}

}
