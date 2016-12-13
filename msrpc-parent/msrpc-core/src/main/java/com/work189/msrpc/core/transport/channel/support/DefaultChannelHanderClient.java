package com.work189.msrpc.core.transport.channel.support;

import com.work189.msrpc.core.rpc.exchange.buffer.rpcbuffer.RpcBuffer;
import com.work189.msrpc.core.rpc.protocol.message.impl.MessageBase;
import com.work189.msrpc.core.transport.channel.Channel;
import com.work189.msrpc.core.transport.channel.swap.ChannelQueue;

public class DefaultChannelHanderClient  extends AbstractChannelHandler{
	@Override
	public void received(Channel channel, Object message) {
		RpcBuffer rpcBuffer = (RpcBuffer)message;
		int requestId = MessageBase.bytes2Int(rpcBuffer.array(), 0);
		ChannelQueue.queue.returnResponse(requestId, rpcBuffer);
	}
	
	@Override
	public void disconnected(Channel channel) {
		
	}
}
