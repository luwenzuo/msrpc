package com.work189.msrpc.core.transport.network.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.work189.msrpc.core.rpc.exchange.HostHander;
import com.work189.msrpc.core.rpc.exchange.buffer.rpcbuffer.RpcBuffer;
import com.work189.msrpc.core.transport.Client;
import com.work189.msrpc.core.transport.channel.Channel;
import com.work189.msrpc.core.transport.channel.ChannelHandler;
import com.work189.msrpc.core.transport.network.mina.encode.MinaProtocolFactory;

public class MinaTransportClient implements Client {
	public final static int MINA_READ_BUFF_SIZE = 8 * 1024;
	private NioSocketConnector connector;
	private volatile IoSession session;

	public MinaTransportClient(HostHander host, ChannelHandler channelHandler) {
		doOpen(host, channelHandler);
		doConnect();
	}

	public synchronized void doOpen(HostHander host,
			ChannelHandler channelHandler) {
		connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(30000);
		connector.setDefaultRemoteAddress(host.getHostAddress());
		connector.getFilterChain().addLast("threadPool", new ExecutorFilter(2,100));
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MinaProtocolFactory()));

		connector.getSessionConfig().setKeepAlive(true);
		connector.getSessionConfig().setReadBufferSize(MINA_READ_BUFF_SIZE);
		connector.getSessionConfig().setMaxReadBufferSize(MINA_READ_BUFF_SIZE);
		connector.getSessionConfig().setSendBufferSize(MINA_READ_BUFF_SIZE);
		
		connector.setHandler(new ClientIoHandlerAdapter(channelHandler));
	}

	public synchronized void doConnect() {
		if (session == null || session.isClosing()) {
			session = connector.connect().awaitUninterruptibly().getSession();
		}
	}

	public synchronized Channel getChannel() {
		return MinaChannel.getOrAddChannel(session);
	}

	private class ClientIoHandlerAdapter extends IoHandlerAdapter {
		private ChannelHandler handler;

		public ClientIoHandlerAdapter(ChannelHandler handler) {
			this.handler = handler;
		}

		public void messageReceived(IoSession session, Object message)
				throws Exception {
			try{
				IoBuffer ioBuffer = (IoBuffer) message;
				handler.received(getChannel(), RpcBuffer.wrap(ioBuffer.array()));
			}catch(Throwable e){
				e.printStackTrace();
			}
		}

		public void sessionClosed(IoSession session) throws Exception {
			MinaChannel.removeChannelIfDisconnectd(session);
	    }
		
		public void exceptionCaught(IoSession session, Throwable cause)
	            throws Exception {
	        session.close(true);
	    }

	}

	@Override
	public void dispose() {
		if(session != null){
			session.close(true);
			session = null;
		}
		if(connector != null){
			connector.dispose();
			connector = null;
		}
		
	}

}
