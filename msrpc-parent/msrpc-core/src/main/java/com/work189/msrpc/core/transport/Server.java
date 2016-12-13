package com.work189.msrpc.core.transport;

import java.util.List;

import com.work189.msrpc.core.rpc.exchange.HostHander;
import com.work189.msrpc.core.transport.channel.Channel;
import com.work189.msrpc.core.transport.channel.ChannelHandler;

public interface Server {
	public void bind(HostHander host, ChannelHandler channelHandler);
	public List<Channel> getChannels();
	public void dispose();
}
