package com.work189.msrpc.core.transport.network.mina;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.work189.msrpc.core.rpc.exchange.HostHander;
import com.work189.msrpc.core.rpc.exchange.buffer.rpcbuffer.RpcBuffer;
import com.work189.msrpc.core.transport.Server;
import com.work189.msrpc.core.transport.channel.Channel;
import com.work189.msrpc.core.transport.channel.ChannelHandler;
import com.work189.msrpc.core.transport.channel.worker.WorkerCount;
import com.work189.msrpc.core.transport.network.mina.encode.MinaProtocolFactory;

public class MinaTransportServer implements Server{
	public final static int MINA_READ_BUFF_SIZE=8*1024;
	public final static WorkerCount workerCount = new WorkerCount("server");

	private IoAcceptor m_acceptor=null;
	public MinaTransportServer(HostHander host, final ChannelHandler channelHandler){
		bind(host, channelHandler);
	}

	@Override
	public void bind(HostHander host, final ChannelHandler channelHandler) {
		try {
			m_acceptor = new NioSocketAcceptor();
			m_acceptor.getSessionConfig().setReadBufferSize( MINA_READ_BUFF_SIZE );
			m_acceptor.getSessionConfig().setMaxReadBufferSize(MINA_READ_BUFF_SIZE);
			m_acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );
			m_acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new MinaProtocolFactory()));
			m_acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(2,100));
			m_acceptor.setHandler( new ServerIoHandlerAdapter(channelHandler));

			m_acceptor.bind(host.getHostAddress());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	protected class ServerIoHandlerAdapter extends IoHandlerAdapter{
		private ChannelHandler handler;
		public ServerIoHandlerAdapter(ChannelHandler channelHandler){
			this.handler = channelHandler;
		}

		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			try {
				//统计
				//workerCount.addAndPrint();
				
				IoBuffer ioBuffer = (IoBuffer)message;
				handler.received(getChannel(session), RpcBuffer.wrap(ioBuffer.array()));
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
			}
		}
		
		public void sessionClosed(IoSession session) throws Exception {
			MinaChannel.removeChannelIfDisconnectd(session);
	    }

		public void sessionOpened(IoSession session) throws Exception {
			handler.connected(getChannel(session));
	    }

		public void exceptionCaught(IoSession session, Throwable cause)
	            throws Exception {
	        session.close(true);
	    }
		
		public Channel getChannel(IoSession session){
			return MinaChannel.getOrAddChannel(session);
		}
	}

	@Override
	public List<Channel> getChannels() {
		List<Channel> channels = new ArrayList<>();
		Map<Long, IoSession> sessions = m_acceptor.getManagedSessions();
		for(Entry<Long, IoSession> item:sessions.entrySet()){
			IoSession session = item.getValue();
			if(session.isConnected()){
				channels.add( new MinaChannel(session) );
			}
		}
		return channels;
	}

	@Override
	public void dispose() {
		m_acceptor.unbind();
		m_acceptor.dispose();
	}

}
