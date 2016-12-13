package com.work189.msrpc.core.zzdemo.xx;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.work189.msrpc.core.transport.network.mina.encode.MinaProtocolFactory;

public class MinaServer {

	public final static int MINA_READ_BUFF_SIZE=4*1024;
	public static long m_total_count=0;
	public static long m_add_count=0;
	public static long m_begin_time=0;
	//public String ip = "192.168.0.11";
	public String ip = "127.0.0.1";

	private IoAcceptor m_acceptor=null;
	public MinaServer(String ip){
		this.ip = ip;
		bind();
	}

	public void bind() {
		try {
			m_acceptor = new NioSocketAcceptor();
			m_acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );
			m_acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(1,1));

			m_acceptor.getSessionConfig().setReadBufferSize( MINA_READ_BUFF_SIZE );
			m_acceptor.getSessionConfig().setMaxReadBufferSize(MINA_READ_BUFF_SIZE);

			m_acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new MinaProtocolFactory()));
			m_acceptor.setHandler( new ExchangeIoHandlerAdapter() {
			});
			
			m_acceptor.getSessionDataStructureFactory();

			m_acceptor.bind(new InetSocketAddress(ip, 15477));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	protected class ExchangeIoHandlerAdapter extends IoHandlerAdapter{
		
		public ExchangeIoHandlerAdapter(){
		}
		
		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			session.write(new byte[529]).awaitUninterruptibly();
			
			//统计
			synchronized (ExchangeIoHandlerAdapter.class) {
				m_total_count ++;
				m_add_count ++;
				
				//每5秒统计
				if(System.currentTimeMillis() - m_begin_time > 5*1000 ){
					int threadCount = Thread.currentThread().getThreadGroup().activeCount();
					System.out.println("threadCount="+threadCount+";total="+m_total_count+";add="+m_add_count);
					m_add_count = 0;
					m_begin_time=System.currentTimeMillis();
				}
			}
		}

		@Override
		public void messageSent(IoSession session, Object message)
				throws Exception {
			//session.close(true);
		}
		
		@Override
		public void exceptionCaught(IoSession session, Throwable cause)
	            throws Exception {
			//session.close(true);
	    }
	}
}
