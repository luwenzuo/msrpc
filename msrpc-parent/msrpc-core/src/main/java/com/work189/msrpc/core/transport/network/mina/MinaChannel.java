package com.work189.msrpc.core.transport.network.mina;

import org.apache.mina.core.session.IoSession;

import com.work189.msrpc.core.rpc.exchange.buffer.rpcbuffer.RpcBuffer;
import com.work189.msrpc.core.transport.channel.Channel;

public class MinaChannel implements Channel{
	private IoSession session = null;
	private static final String CHANNEL_KEY = MinaChannel.class.getName() + ".CHANNEL";
	
	public MinaChannel(IoSession session){
		this.session = session;
	}

	public void send(Object message, boolean sent){
		RpcBuffer rpcBuffer = (RpcBuffer) message;
		this.session.write(rpcBuffer.array());
	}
	
	public static MinaChannel getOrAddChannel(IoSession session){
		if(session == null){
			return null;
		}
		
		MinaChannel ret = (MinaChannel) session.getAttribute(CHANNEL_KEY);
		if(ret == null){
			if(session.isConnected()){
				ret = new MinaChannel(session);
				session.setAttribute(CHANNEL_KEY, ret);
			}else{
				return null;
			}
		}
		return ret;
	}
	
	public static void removeChannelIfDisconnectd(IoSession session) {
        if (session != null && ! session.isConnected()) {
            session.removeAttribute(CHANNEL_KEY);
        }
    }
}
