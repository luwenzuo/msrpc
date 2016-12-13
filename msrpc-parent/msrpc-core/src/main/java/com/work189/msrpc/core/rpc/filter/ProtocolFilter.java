package com.work189.msrpc.core.rpc.filter;

import com.work189.msrpc.core.rpc.protocol.message.MessageFactory;


public interface ProtocolFilter {

	public boolean filter(MessageFactory factory);
}
