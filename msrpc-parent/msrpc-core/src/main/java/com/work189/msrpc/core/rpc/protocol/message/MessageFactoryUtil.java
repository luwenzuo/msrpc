package com.work189.msrpc.core.rpc.protocol.message;

import com.work189.msrpc.core.rpc.exchange.buffer.rpcbuffer.RpcBuffer;
import com.work189.msrpc.core.rpc.protocol.ProtocolManager;
import com.work189.msrpc.core.rpc.protocol.message.impl.MessageConstant;
import com.work189.msrpc.core.rpc.protocol.message.impl.MessagePackUtil;
import com.work189.msrpc.core.rpc.protocol.message.impl.MessageReadUtil;

public class MessageFactoryUtil {
	
	public static byte[] packByte(MessageFactory factory){
		MessagePackUtil msg = new MessagePackUtil();
		return msg.pack(factory.getId(), factory.getFields() );
	}
	public static RpcBuffer packBuffer(MessageFactory factory){
		return RpcBuffer.wrap( packByte(factory) );
	}

	public static void readByte(MessageFactory factory, byte[] data){
		MessageReadUtil msg = new MessageReadUtil();
		int id = msg.read(data);
		factory.setFields(msg.getFields());
		factory.setId(id);
	}
	public static void readBuffer(MessageFactory factory, RpcBuffer rpcBuffer){
		readByte(factory, rpcBuffer.array());
	}
	
	public static Object getServiceBufferData(MessageFactory factory){
		return ProtocolManager.getSerializer().deserialize(
				factory.getByte(MessageConstant.SERVICE_BUFFER_DATA));
	}
	
	public static void setServiceBufferData(MessageFactory factory, Object object){
		factory.putByte(MessageConstant.SERVICE_BUFFER_DATA, 
				ProtocolManager.getSerializer().serialize(object));
	}
}
