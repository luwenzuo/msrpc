package com.work189.msrpc.core.transport.channel;

public interface ChannelHandler {

	public void connected(Channel channel);

	public void disconnected(Channel channel);

	public void sent(Channel channel, Object message);

	public void received(Channel channel, Object message);

	public void caught(Channel channel, Throwable exception);
}
