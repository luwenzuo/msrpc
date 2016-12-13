package com.work189.msrpc.core.transport.channel.swap;

public class ChannelCodec {
	public final static int PACK_LENGTH = 3;

	public static byte[] encode(byte []data){
		byte []buf = new byte[data.length+PACK_LENGTH];
		System.arraycopy(int2Bytes(data.length), 0, buf, 0, PACK_LENGTH);
		System.arraycopy(data, 0, buf, PACK_LENGTH, data.length);
		return buf;
	}

	public static void decode(byte []data){
		
	}
	
	public static byte[] int2Bytes(int value) {
		byte[] src = new byte[PACK_LENGTH];
		src[2] = (byte) ((value >> 16) & 0xFF);
		src[1] = (byte) ((value >> 8) & 0xFF);
		src[0] = (byte) (value & 0xFF);
		return src;
	}

	public static int bytes2Int(byte[] b) {
		byte[] res = b;
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00)
				| ((res[2] << 24) >>> 8);
		return targets;
	}

}
