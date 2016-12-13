package com.work189.msrpc.core.config.variable;

import java.io.File;

import com.work189.msrpc.core.common.file.MSFileUtil;

public class SystemDefine {

	public static String getRunTempDirectory(){
		return MSFileUtil.getPath() + File.separator + "runtemp";
	}
}
