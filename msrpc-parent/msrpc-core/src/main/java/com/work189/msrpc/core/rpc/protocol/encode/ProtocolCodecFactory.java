package com.work189.msrpc.core.rpc.protocol.encode;

public interface ProtocolCodecFactory {

	public ProtocolCodeEncoder getEncoder();
	public ProtocolCodeDecoder getDecoder();
}
