package com.work189.msrpc.core.rpc.protocol.message.impl;

import java.util.HashMap;
import java.util.Map;

import com.work189.msrpc.core.rpc.protocol.message.MessageField;

public class MessageReadUtil extends MessageBase{
	
	protected int m_pos=0;
	protected byte []m_data;
	protected int m_msgLength=0;
	protected Map<Integer , MessageField> m_fields = new HashMap<>();

	public Map<Integer , MessageField> getFields(){
		return this.m_fields;
	}
	
	public int read(byte[] data){
		m_pos = 0;
		m_data = data;
		
		//解析报文长度
		int id=readMsgId();
		
		m_msgLength = m_data.length;
		while(m_pos < m_msgLength && (m_msgLength-m_pos)>=8){
			loopRead();
		}
		return id;
	}

	private void loopRead(){
		MessageField field = new MessageField();
		
		//域编号
		field.setId( bytes2Int(m_data, m_pos) );
		m_pos += 4;

		//数据长度
		int dataLength = bytes2Int(m_data, m_pos);
		m_pos += 4;
		
		if((m_msgLength-m_pos) < dataLength){
			throw new RuntimeException("数据格式错误");
		}

		//数据内容
		byte []dataContext = new byte[dataLength];
		System.arraycopy(m_data, m_pos, dataContext, 0, dataLength);
		field.setData(dataContext);
		m_pos += dataLength;

		m_fields.put(field.getId(), field);
	}
	
	protected int readMsgId(){
		m_pos = 0;
		int id = bytes2Int(m_data, m_pos);
		m_pos += 4;
		return id;
	}
}
