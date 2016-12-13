package com.work189.msrpc.core.transport.channel.support;

import com.work189.msrpc.core.transport.channel.Channel;
import com.work189.msrpc.core.transport.channel.ChannelHandler;

public abstract class AbstractChannelHandler implements ChannelHandler{

	@Override
	public void connected(Channel channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnected(Channel channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sent(Channel channel, Object message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void received(Channel channel, Object message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void caught(Channel channel, Throwable exception) {
		// TODO Auto-generated method stub
		
	}

}
