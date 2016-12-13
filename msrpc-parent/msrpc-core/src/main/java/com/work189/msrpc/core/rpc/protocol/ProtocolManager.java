package com.work189.msrpc.core.rpc.protocol;

import com.work189.msrpc.core.rpc.protocol.serialize.ProtocolHession;
import com.work189.msrpc.core.rpc.protocol.serialize.ProtocolSerialize;

public class ProtocolManager {
	
	
	public final static ProtocolHession protocolHession = new ProtocolHession();
	public static ProtocolSerialize getSerializer(){
		return protocolHession;
	}
}
