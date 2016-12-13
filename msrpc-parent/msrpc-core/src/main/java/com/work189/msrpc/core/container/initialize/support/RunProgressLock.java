package com.work189.msrpc.core.container.initialize.support;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.work189.msrpc.core.common.system.MSProgress;
import com.work189.msrpc.core.config.variable.SystemDefine;

public class RunProgressLock {

	private static RandomAccessFile randomAccessFile=null;

	public static boolean lockProgress() {
		String filename = SystemDefine.getRunTempDirectory()+File.separator+RunProgressLock.class.getName()+".txt";
		try {
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Throwable e) {
			return false;
		}
		
		return lockFile(filename);
	}
	public static boolean unlockProgress() {
		try {
			randomAccessFile.close();
		} catch (IOException e) {
		}
		return true;
	}

	private synchronized static boolean lockFile(String filename){
		try{
			if(randomAccessFile != null){
				return false;
			}
			randomAccessFile = new RandomAccessFile(filename,"rw");
			
			//randomAccessFile.write(String.format("%08d", 32132).getBytes());
			
			FileChannel fileChannel = randomAccessFile.getChannel();
			if( fileChannel.tryLock() == null){
				return true;
			}
			
			//保存进程号
			fileChannel.write( ByteBuffer.wrap(String.format("%08d", MSProgress.getProgressId() ).getBytes()) ); 
			
		}catch(Throwable e){
			
		}
		return false;
	}
}
