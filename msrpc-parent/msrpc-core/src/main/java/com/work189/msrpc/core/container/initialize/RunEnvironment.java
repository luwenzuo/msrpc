package com.work189.msrpc.core.container.initialize;

import java.io.File;

import com.work189.msrpc.core.common.file.MSFileUtil;
import com.work189.msrpc.core.common.log.LoggerFactory;
import com.work189.msrpc.core.container.initialize.support.RunProgressLock;

public class RunEnvironment {

	public static void initialize(){
		
		if(RunProgressLock.lockProgress()){
			throw new RuntimeException("服务程序不允许重复执行");
		}
		
		//初始文件运行目录
		String path = MSFileUtil.getPath()+File.separator+"runtemp";
		MSFileUtil.mkdir(path);
		
		LoggerFactory.getLogger(RunEnvironment.class).info( path );
	}
}
