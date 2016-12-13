package com.work189.msrpc.core.rpc.protocol.message.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.work189.msrpc.core.rpc.protocol.message.MessageField;

public class MessagePackUtil extends MessageBase{
	
	protected int m_pos=0;
	protected byte []m_data;
	protected Map<Integer , MessageField> m_fields = new HashMap<>();

	public Map<Integer , MessageField> getFields(){
		return this.m_fields;
	}
	
	public byte[] pack(int id, Map<Integer , MessageField> fields){
		
		this.m_fields = fields;

		//打包报文头
		packMsgHead(id);
		
		//数据包长度
		for(Entry<Integer, MessageField> entry:this.getFields().entrySet()){
			loopPack( entry.getValue() );
		}
		return m_data;
	}

	private void loopPack(MessageField field){
		
		//域编号
		System.arraycopy(int2Bytes(field.getId()), 0, m_data, m_pos, 4);
		m_pos += 4;

		//数据长度
		System.arraycopy(int2Bytes(field.getLength()), 0, m_data, m_pos, 4);
		m_pos += 4;
		
		//数据内容
		System.arraycopy(field.getData(), 0, m_data, m_pos, field.getLength());
		m_pos += field.getLength();
	}
	
	private void packMsgHead(int id){
		m_pos = 0;
		m_data = new byte[getAllMessageLength()+4];
		System.arraycopy(int2Bytes(id), 0, m_data, m_pos, 4);
		m_pos += 4;
	}
	
	protected int getAllMessageLength(){
		int ncount = 0;
		//计算总长度
		for(Entry<Integer, MessageField> entry:this.getFields().entrySet()){
			MessageField field = entry.getValue();
			ncount += field.getLength()+4+4;
		}
		return ncount;
	}
}
