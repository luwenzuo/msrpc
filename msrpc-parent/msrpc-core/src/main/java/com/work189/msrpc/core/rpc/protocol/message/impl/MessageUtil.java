package com.work189.msrpc.core.rpc.protocol.message.impl;

import java.util.Map;

import com.work189.msrpc.core.rpc.protocol.message.MessageField;

public class MessageUtil {


	public static void putByte(Map<Integer , MessageField> fields, int id, byte []data){
		MessageField field = new MessageField();
		field.setId(id);
		field.setData(data);
		fields.put(field.getId(), field);
	}
	
	public static byte[] getByte(Map<Integer , MessageField> fields, int id){
		MessageField field = fields.get( id );
		if(field == null){
			return null;
		}
		return field.getData();
	}
}
