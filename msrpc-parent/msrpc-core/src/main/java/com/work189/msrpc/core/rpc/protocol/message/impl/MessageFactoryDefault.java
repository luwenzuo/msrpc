package com.work189.msrpc.core.rpc.protocol.message.impl;

import java.util.HashMap;
import java.util.Map;

import com.work189.msrpc.core.rpc.protocol.message.MessageFactory;
import com.work189.msrpc.core.rpc.protocol.message.MessageField;

public class MessageFactoryDefault implements MessageFactory{

	private int id;
	protected Map<Integer , MessageField> fields = new HashMap<>();

	public int getId() {
		return this.id;
	}
	
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	public void putByte(Integer id, byte[] data){
		MessageUtil.putByte(fields, id, data);
	}
	
	public byte[] getByte(Integer id){
		return MessageUtil.getByte(fields, id);
	}

	public Map<Integer, MessageField> getFields() {
		return fields;
	}

	public void setFields(Map<Integer, MessageField> fields) {
		this.fields = fields;
	}
	
}
