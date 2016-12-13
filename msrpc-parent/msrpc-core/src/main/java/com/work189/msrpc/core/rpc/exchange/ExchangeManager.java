package com.work189.msrpc.core.rpc.exchange;

import java.util.ArrayList;
import java.util.List;

import com.work189.msrpc.core.common.log.Logger;
import com.work189.msrpc.core.common.log.LoggerFactory;
import com.work189.msrpc.core.config.springboot.schema.host.HostUtil;
import com.work189.msrpc.core.registry.RegistryCenter;
import com.work189.msrpc.core.registry.entity.RegistryHostEntity;
import com.work189.msrpc.core.rpc.exchange.host.DefaultExchangeHostConfig;
import com.work189.msrpc.core.rpc.exchange.host.ExchangeHostHander;
import com.work189.msrpc.core.rpc.exchange.host.LocalhostPortLock;
import com.work189.msrpc.core.rpc.protocol.encode.ProtocolCodecFactory;
import com.work189.msrpc.core.transport.Client;
import com.work189.msrpc.core.transport.Server;
import com.work189.msrpc.core.transport.channel.ChannelHandler;
import com.work189.msrpc.core.transport.channel.support.DefaultChannelHanderServer;
import com.work189.msrpc.core.transport.network.mina.MinaTransporter;

public class ExchangeManager {
	private final static Logger logger = LoggerFactory.getLogger(ExchangeManager.class);
	public static MinaTransporter transporter = new MinaTransporter();

	public static Client getClient(HostHander host, ChannelHandler channelHandler){
		return transporter.getClient(host, channelHandler);
	}
	
	public static Server getServer(HostHander host, ChannelHandler channelHandler){
		return transporter.getServer(host, channelHandler);
	}

	private static Server defaultServer = null;

	public static synchronized Server getDefaultServerListen(){
		if(defaultServer == null){
			try{
				LocalhostPortLock.lock();
				defaultServer = getDefaultServerNotLock();
			}finally{
				LocalhostPortLock.unlock();
			}
		}
		return defaultServer;
	}

	private static synchronized Server getDefaultServerNotLock(){
		if(defaultServer == null){
			int defaultPort = LocalhostPortLock.readLocalPort();
			int port = defaultPort;
			while(port < defaultPort+1000){
				DefaultExchangeHostConfig.config.setHostPort(port);
				boolean ret = HostUtil.isPortUsing(
						DefaultExchangeHostConfig.config.getHostIp(), 
						DefaultExchangeHostConfig.config.getHostPort());
				if(ret == true){
					break;
				}
				port++;
			}
			LocalhostPortLock.saveLocalPort(port);

			ExchangeHostHander host = new ExchangeHostHander();
			host.setHostAddress(
					DefaultExchangeHostConfig.config.getHostIp(), 
					DefaultExchangeHostConfig.config.getHostPort());
			
			ChannelHandler channelHander = new DefaultChannelHanderServer();
			Server server = ExchangeManager.getServer(host, channelHander);
			defaultServer = server;

			RegistryHostEntity hostEntity = new RegistryHostEntity();
			hostEntity.setHostIP(DefaultExchangeHostConfig.config.getHostIp());
			hostEntity.setHostPort(DefaultExchangeHostConfig.config.getHostPort());
			hostEntity.setHostKey(DefaultExchangeHostConfig.config.getHostKey());
			hostEntity.setActiveKey(DefaultExchangeHostConfig.config.getActiveKey());
			RegistryCenter.master.addServer(hostEntity.getHostKey(), server, hostEntity);
			
			logger.info("服务器侦听地址["+
					DefaultExchangeHostConfig.config.getHostIp()+":"+
					DefaultExchangeHostConfig.config.getHostPort() +"]");
		}
		return defaultServer;
	}
	
	public static List<ProtocolCodecFactory> getProtocolCodecs(){
		return new ArrayList<ProtocolCodecFactory>();
	}
}
