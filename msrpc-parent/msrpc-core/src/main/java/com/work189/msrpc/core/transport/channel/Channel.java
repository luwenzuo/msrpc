package com.work189.msrpc.core.transport.channel;

public interface Channel {

	public void send(Object message, boolean sent);
	
}
