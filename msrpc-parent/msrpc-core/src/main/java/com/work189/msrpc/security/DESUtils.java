package com.work189.msrpc.security;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESUtils {

	/**
	 * 加密
	 * */
	public static String encrypt(String key, String data) {
		try {
			key = fillString(key);
			byte []byteData = data.getBytes();//Base64Utils.encode(data.getBytes());
			
			byteData = crypt(Cipher.ENCRYPT_MODE, key.getBytes(), byteData);
			return StringHex.bytesToHexString(byteData);//Base64Utils.encodeToString(byteData);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解密
	 * */
	public static String decrypt(String key, String data) {
		try{
			key = fillString(key);
			byte []byteData = StringHex.hexStringToBytes(data);//Base64Utils.decodeFromString(data);
			byteData = crypt(Cipher.DECRYPT_MODE, key.getBytes(), byteData);
			return new String(byteData);//new String(Base64Utils.decode(byteData));
		}catch(Throwable e){
			e.printStackTrace();
			return null;
		}
	}
	public static byte[] crypt(int flag, byte []key, byte []data) {
		try{
			
			// DES算法要求有一个可信任的随机数源
			SecureRandom random = new SecureRandom();
			// 创建一个DESKeySpec对象
			DESKeySpec desKey = new DESKeySpec( key );
			// 创建一个密匙工厂DES/ECB/NoPadding
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// 将DESKeySpec对象转换成SecretKey对象
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象 Cipher.DECRYPT_MODE
			cipher.init(flag, securekey, random);
			
			// 真正开始解密操作
			return cipher.doFinal( data );
		}catch(Throwable e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 填充数据
	 * */
	public static String fillString(String data){
		int len = data.length()%8;
		for(int i=0;i<len;i++){
			data = data + " ";
		}
		return data;
	}
	
	public static void main(String[] args) throws Exception {
		String key = "9vYiwpYmIpvUQrmZyGLaiK1NOZHRGq0Z";
		String data = "123456";
		String vv;
		
		System.out.println("数据:"+data);
		vv = DESUtils.encrypt(key, data);
		System.out.println("加密:"+vv);
		vv = DESUtils.decrypt(key, vv);
		System.out.println("解密:"+vv);
    }
}
