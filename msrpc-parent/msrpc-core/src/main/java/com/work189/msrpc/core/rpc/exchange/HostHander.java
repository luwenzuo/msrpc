package com.work189.msrpc.core.rpc.exchange;

import java.net.InetSocketAddress;

public interface HostHander {
	public InetSocketAddress getHostAddress();
	public String getHostKey();
}
