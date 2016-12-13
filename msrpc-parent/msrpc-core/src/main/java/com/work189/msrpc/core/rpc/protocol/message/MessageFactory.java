package com.work189.msrpc.core.rpc.protocol.message;

import java.util.Map;

public interface MessageFactory {
	public int getId();
	public void setId(int id);
	
	public void putByte(Integer id, byte[] data);

	public byte[] getByte(Integer id);

	public Map<Integer, MessageField> getFields();

	public void setFields(Map<Integer, MessageField> fields);
}
