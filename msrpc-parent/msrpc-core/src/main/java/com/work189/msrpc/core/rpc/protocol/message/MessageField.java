package com.work189.msrpc.core.rpc.protocol.message;

public class MessageField {
	//长度标记:0x00~0xff,前四位表示id编号所占字节,后四位表示数据长度所占字节
	//private int mark;
	//域编号
	private int id=0;
	//数据字节
	private byte[] data=null;
	
	public MessageField(){
		
	}
	
	public MessageField(int id, byte []data){
		this.id = id;
		this.data = data;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public int getLength(){
		if(this.data == null){
			return 0;
		}
		return this.data.length;
	}
}
