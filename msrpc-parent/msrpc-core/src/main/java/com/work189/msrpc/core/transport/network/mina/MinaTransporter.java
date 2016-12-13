package com.work189.msrpc.core.transport.network.mina;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.work189.msrpc.core.rpc.exchange.HostHander;
import com.work189.msrpc.core.transport.Client;
import com.work189.msrpc.core.transport.Server;
import com.work189.msrpc.core.transport.Transporter;
import com.work189.msrpc.core.transport.channel.ChannelHandler;

public class MinaTransporter implements Transporter{
	
	protected Map<String, Client> clientsMap = new ConcurrentHashMap<String, Client>();
	protected Map<String, Server> serverMap = new ConcurrentHashMap<String, Server>();

	@Override
	public Server getServer(HostHander host, ChannelHandler channelHandler) {
		Server server = serverMap.get( host.getHostKey() );
		if(server == null){
			server = new MinaTransportServer(host, channelHandler);
			serverMap.put(host.getHostKey(), server);
		}
		return server;
	}

	@Override
	public synchronized Client getClient(HostHander host, ChannelHandler channelHandler) {
		Client client = clientsMap.get(host.getHostKey());
		if(client == null){
			client = new MinaTransportClient(host, channelHandler);
			clientsMap.put(host.getHostKey(), client);
		}
		if(client.getChannel() == null){
			client.dispose();
			clientsMap.remove(host.getHostKey());
			//重新链接
			client = new MinaTransportClient(host, channelHandler);
			clientsMap.put(host.getHostKey(), client);
		}
		return client;
	}

	@Override
	public void destroy() {
		for(Entry<String, Client> entry:clientsMap.entrySet()){
			Client client = entry.getValue();
			client.dispose();
		}
		for(Entry<String, Server> entry:serverMap.entrySet()){
			Server server = entry.getValue();
			server.dispose();
		}
	}

}
