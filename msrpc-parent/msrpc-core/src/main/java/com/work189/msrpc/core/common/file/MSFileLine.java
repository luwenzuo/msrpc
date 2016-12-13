package com.work189.msrpc.core.common.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.work189.msrpc.core.config.variable.SystemDefine;

public class MSFileLine {
	
	public static int readInt(String filename){
		try{
			String s = readString(filename);
			return Integer.parseInt(s);
		}catch(Throwable e){
			throw new RuntimeException(e.getMessage());
		}
	}
	public static void saveInt(String filename, int value){
		writeString(filename, String.format("%08d", value));
	}

	public static String readString(String filename) {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			File file = new File(filename);
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String str = br.readLine();
			if(str != null){
				str = str.replaceAll("\r", "");
				str = str.replaceAll("\n", "");
			}
			return str;
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage());
		}finally{
			try {
				br.close();
			} catch (IOException e1) {
			}
			try {
				fr.close();
			} catch (IOException e) {
			}
		}
	}
	
	public static void writeString(String filename, String value) {
		FileOutputStream fos = null;
		try {
			if (!filename.startsWith(SystemDefine.getRunTempDirectory())) {
				throw new RuntimeException("文件目录不符合要求" + filename);
			}

			File file = new File(filename);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(value.getBytes());
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage());
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
		
	}
}
