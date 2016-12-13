package com.work189.msrpc.core.transport;

import com.work189.msrpc.core.transport.channel.Channel;

public interface Client {
	public Channel getChannel();

	public void dispose();
}
