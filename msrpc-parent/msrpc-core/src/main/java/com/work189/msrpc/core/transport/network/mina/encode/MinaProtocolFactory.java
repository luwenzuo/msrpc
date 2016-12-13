package com.work189.msrpc.core.transport.network.mina.encode;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MinaProtocolFactory implements ProtocolCodecFactory{
	
	protected final MinaProtocolDecoder decoder = new MinaProtocolDecoder();
	protected final MinaProtocolEncoder encoder = new MinaProtocolEncoder();

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return decoder;
	}

}
