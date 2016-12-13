package com.work189.msrpc.core.rpc.protocol.encode;

import com.work189.msrpc.core.rpc.protocol.message.MessageFactory;

public interface ProtocolCodeDecoder {
	
	public void decode(MessageFactory messageFactory);

}
