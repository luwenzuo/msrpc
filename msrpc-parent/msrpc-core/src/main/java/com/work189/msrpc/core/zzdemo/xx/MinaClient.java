package com.work189.msrpc.core.zzdemo.xx;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.work189.msrpc.core.transport.network.mina.encode.MinaProtocolFactory;

public class MinaClient extends Thread {
	public final static int MINA_READ_BUFF_SIZE = 32*1024*1024;
	// public String ip = "192.168.0.11";
	public String ip = "127.0.0.1";

	public MinaClient(String ip) {
		this.ip = ip;
	}

	@Override
	public void run() {
		System.out.println("线程启动");
		loadTest();
	}

	public void loadTest() {
		try {

			while (true) {
				try {
					call();
				} catch (Throwable e) {
					e.printStackTrace();
					Thread.sleep(10 * 1000);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	ThreadLocal<NioSocketConnector> connectors = new ThreadLocal<NioSocketConnector>();
	ThreadLocal<IoSession> sessions = new ThreadLocal<IoSession>();

	public static long m_total_count=0;
	public static long m_add_count=0;
	public static long m_begin_time=0;
	public static long m_data_size=0;
	public static int m_data_buffer=0;
	public void call() {

		try {
			NioSocketConnector connector = connectors.get();
			if (connector == null) {
				connector = new NioSocketConnector();
				
				connector.setConnectTimeoutMillis(30000);
				connector.getSessionConfig().setReadBufferSize(
						MINA_READ_BUFF_SIZE);
				connector.getSessionConfig().setKeepAlive(true);
				connector.getSessionConfig().setMaxReadBufferSize(MINA_READ_BUFF_SIZE);
				connector.getSessionConfig().setSendBufferSize(MINA_READ_BUFF_SIZE);
				connector.getFilterChain().addLast("threadPool", new ExecutorFilter(1,10));
				
				// connector.getSessionConfig().setSoLinger(0);
				connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MinaProtocolFactory()));

				connector.setHandler(new IoHandlerAdapter() {
					public void sessionOpened(IoSession session)
							throws Exception {
						// Empty handler
					}

					public void messageReceived(IoSession session,
							Object message) throws Exception {
						/*
						try{
							//统计
							synchronized (MinaClient.class) {
								m_total_count ++;
								m_add_count ++;
								
								//每5秒统计
								if(System.currentTimeMillis() - m_begin_time > 5*1000){
									int threadCount = Thread.currentThread().getThreadGroup().activeCount();
									System.out.println("threadCount="+threadCount+";total="+m_total_count+";add="+m_add_count);
									m_add_count = 0;
									m_begin_time=System.currentTimeMillis();
								}
							}
							
							//byte[] buf = (byte[]) message;
							//System.out.println("client-messageReceived=");
						}catch(Throwable e){
							e.printStackTrace();
						}*/
					}

					public void messageSent(IoSession session, Object message)
							throws Exception {
					}
				});
				connectors.set(connector);
				IoSession session = connector
						.connect(new InetSocketAddress(ip, 15477))
						.awaitUninterruptibly().getSession();
				sessions.set(session);
			}
			IoSession session = sessions.get();
			
			// 请求数据
			m_data_buffer = 240;
			byte[] buf = new byte[(int) m_data_buffer];
			WriteFuture wf = session.write(buf);

			wf.awaitUninterruptibly();

			
		} finally {
		}
		
		try {
			//Thread.sleep(1);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		//统计
		synchronized (MinaClient.class) {
			m_total_count ++;
			m_add_count ++;
			m_data_size += m_data_buffer;
			
			//每5秒统计
			if(System.currentTimeMillis() - m_begin_time > 5*1000){
				int threadCount = Thread.currentThread().getThreadGroup().activeCount();
				System.out.println("threadCount="+threadCount+";total="+m_total_count+";add="+m_add_count+";SP="+m_data_size/1024/1024/5+"M/S");
				m_add_count = 0;
				m_data_size = 0;
				m_begin_time=System.currentTimeMillis();
			}
		}
	}

	public void call2() {

		try {
			NioSocketConnector connector = connectors.get();
			if (connector == null) {
				connector = new NioSocketConnector();
			}
			connector.setConnectTimeoutMillis(30000);
			connector.getSessionConfig().setReadBufferSize(MINA_READ_BUFF_SIZE);
			// connector.getSessionConfig().setSoLinger(0);
			connector.getFilterChain().addLast("codec",
					new ProtocolCodecFilter(new MinaProtocolFactory()));

			connector.setHandler(new IoHandlerAdapter() {
				public void sessionOpened(IoSession session) throws Exception {
					// Empty handler
				}

				@SuppressWarnings("unused")
				public void messageReceived(IoSession session, Object message)
						throws Exception {
					try{
						System.out.println("messageReceived-client");
						// IoBuffer iobuffer = (IoBuffer)message;
						// m_size += iobuffer.limit();
						byte[] buf = (byte[]) message;
						//System.out.println("client-messageReceived=" + buf.length);
					}catch(Throwable e){
						e.printStackTrace();
					}
				}

				public void messageSent(IoSession session, Object message)
						throws Exception {

					// System.out.println("messageSent");
				}
			});

			IoSession session = connector
					.connect(new InetSocketAddress(ip, 15477))
					.awaitUninterruptibly().getSession();
			// 请求数据
			byte[] buf = new byte[256];
			session.write(buf).awaitUninterruptibly();
			session.getCloseFuture().awaitUninterruptibly();
			session.close(true);
			connector.dispose();
		} finally {
		}
	}
}
