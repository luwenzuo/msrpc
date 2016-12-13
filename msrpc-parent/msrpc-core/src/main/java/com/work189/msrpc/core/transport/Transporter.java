package com.work189.msrpc.core.transport;

import com.work189.msrpc.core.rpc.exchange.HostHander;
import com.work189.msrpc.core.transport.channel.ChannelHandler;


public interface Transporter {

	public Server getServer(HostHander host, ChannelHandler channelHandler);
	public Client getClient(HostHander host, ChannelHandler channelHandler);
	public void destroy();
}
