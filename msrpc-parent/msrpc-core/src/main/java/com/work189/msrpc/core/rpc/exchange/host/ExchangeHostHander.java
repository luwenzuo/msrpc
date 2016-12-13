package com.work189.msrpc.core.rpc.exchange.host;

import java.net.InetSocketAddress;

import com.work189.msrpc.core.rpc.exchange.HostHander;

public class ExchangeHostHander implements HostHander{

	protected InetSocketAddress hostAddress;

	public InetSocketAddress setHostAddress(String ip, int port){
		this.hostAddress = new InetSocketAddress(ip, port);
		return this.hostAddress;
	}

	@Override
	public InetSocketAddress getHostAddress() {
		return this.hostAddress;
	}

	@Override
	public String getHostKey() {
		String key = this.hostAddress.getHostString()+":"+this.hostAddress.getPort();
		return key;
	}
}
