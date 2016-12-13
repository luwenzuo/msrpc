package com.work189.msrpc.core.rpc.protocol.encode;

import com.work189.msrpc.core.rpc.protocol.message.MessageFactory;

public interface ProtocolCodeEncoder {

	public void encode(MessageFactory messageFactory);
	
}
