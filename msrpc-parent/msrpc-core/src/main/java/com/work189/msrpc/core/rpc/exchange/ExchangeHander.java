package com.work189.msrpc.core.rpc.exchange;

import com.work189.msrpc.core.rpc.exchange.buffer.rpcbuffer.RpcBuffer;

public interface ExchangeHander {
	public RpcBuffer callService(RpcBuffer request);
}
