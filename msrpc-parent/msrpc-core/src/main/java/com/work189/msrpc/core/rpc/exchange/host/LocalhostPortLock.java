package com.work189.msrpc.core.rpc.exchange.host;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.work189.msrpc.core.common.file.MSFileLine;
import com.work189.msrpc.core.config.variable.SystemDefine;

public class LocalhostPortLock {
	private static ThreadLocal<Socket> localSocket = new ThreadLocal<Socket>();
	private static String localPortFile = SystemDefine.getRunTempDirectory()+File.separator + LocalhostPortLock.class.getName()+".txt";

	public static synchronized void lock() {
		try {
			long lBeginTime = System.currentTimeMillis();
			while (true) {
				if (System.currentTimeMillis() - lBeginTime > 10 * 1000) {
					break;
				}

				try {
					Socket socket = localSocket.get();
					if (socket == null) {
						socket = new Socket();
						SocketAddress localAddr = new InetSocketAddress("127.0.0.1", 6122);
						socket.bind(localAddr);
						localSocket.set(socket);
						return;
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
				Thread.sleep(System.currentTimeMillis()%1000);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		throw new RuntimeException("共享锁失败");
	}
	
	public synchronized static void unlock(){
		Socket socket = localSocket.get();
		if(socket != null){
			try {
				socket.close();
			} catch (IOException e) {
				socket = null;
			}
		}
		localSocket.set(null);
	}

	public static void saveLocalPort(int port) {
		MSFileLine.saveInt(localPortFile, port);
	}
	
	public static int readLocalPort() {

		try {
			int port = MSFileLine.readInt(localPortFile);
			if (port > 1024
					&& port < DefaultExchangeHostConfig.config.getHostPort() + 1000) {
				// 控制端口范围
				return port;
			}
		} catch (Throwable e) {
		}

		int defaultPort = DefaultExchangeHostConfig.config.getHostPort();
		return defaultPort;
	}
}
