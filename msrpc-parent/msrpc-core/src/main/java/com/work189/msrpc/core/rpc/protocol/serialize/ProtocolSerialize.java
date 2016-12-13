package com.work189.msrpc.core.rpc.protocol.serialize;

public interface ProtocolSerialize {

	public byte[] serialize(Object object);
	public Object deserialize(byte []data);
}
