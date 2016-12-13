package com.work189.msrpc.core.transport.channel.swap;

import java.util.ArrayList;
import java.util.List;

public class ChannelBuffer {
	private List<byte[]> dataList = new ArrayList<>();

	public ChannelBuffer() {
	}
	
	public byte[] get(){
		return readFirstData();
	}

	public void put(byte[] in) {
		if(in == null || in.length < 1){
			return;
		}
		dataList.add(in);
	}
	
	private byte[] readFirstData(){
		int dataSize = getFirstDataSize();
		if(dataSize < 1){
			return null;
		}

		int pos = 0;
		int left = dataSize + ChannelCodec.PACK_LENGTH;
		byte []dataByte = new byte[left];
		for(int i=0; i<dataList.size(); ){
			byte []b = dataList.get(i);
			
			//报文体
			if(b.length <= left){
				System.arraycopy(b, 0, dataByte, pos, b.length);
				pos += b.length;
				left -= b.length;
				dataList.remove(i);
			}else{
				System.arraycopy(b, 0, dataByte, pos, left);
				byte []temp = new byte[b.length-left];
				System.arraycopy(b, left, temp, 0, temp.length);
				dataList.set(i, temp);
				pos += left;
				left -= left;
				i++;
			}
			if(left < 1){
				break;
			}
		}
		
		byte []retByte = new byte[dataSize];
		System.arraycopy(dataByte, ChannelCodec.PACK_LENGTH, retByte, 0, dataSize);
		
		return retByte;
	}
	
	private int getFirstDataSize(){
		int dataSize = 0;
		
		//数据长度
		int headSize = 0;
		byte []headByte = new byte[ChannelCodec.PACK_LENGTH];
		for(byte []b : dataList){
			if(b.length+headSize <= ChannelCodec.PACK_LENGTH){
				System.arraycopy(b, 0, headByte, headSize, b.length);
				headSize += b.length;
			}else{
				int left = ChannelCodec.PACK_LENGTH-headSize;
				System.arraycopy(b, 0, headByte, headSize, left);
				headSize += left;
			}
			if(headSize >= ChannelCodec.PACK_LENGTH){
				dataSize = ChannelCodec.bytes2Int(headByte);
			}
		}
		if(dataSize < 0){
			return 0;
		}
		
		//当前数据量
		int currentSize = 0;
		for(byte []b : dataList){
			currentSize += b.length;
		}
		//System.out.println("currentSize="+currentSize+";dataSize="+dataSize);
		
		if(currentSize >= (dataSize+ChannelCodec.PACK_LENGTH)){
			return dataSize;
		}else{
			return 0;
		}
		
	}

}
