package com.work189.msrpc.core.config.springboot.schema.host;

import java.io.File;
import java.net.URL;
import java.util.Properties;
import java.util.UUID;

public class RuntimeProperties {

	
	public RuntimeProperties(){
	}
	
	public String getRuntimeFilename(){
		try{
			URL url = HostUtil.class.getClassLoader().getResource("");
			File file = new File(url.toURI());
			String filename = file.getPath() + File.separator+"hostrun.properties";
			file = new File(filename);
			if(!file.exists()){
				file.createNewFile();
			}
			return filename;
		}catch(Throwable e){
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public String getProperty(String key){

		try{
			String filename = getRuntimeFilename();
			Properties properties = HostUtil.readProperties(filename);
			String hostKey = properties.getProperty(key);
			return hostKey;
		}catch(Throwable e){
			throw new RuntimeException(e.getMessage());
		}finally{
		}
	}
	
	public void saveProperty(String key, String value){
		try{
			String filename = getRuntimeFilename();
			Properties properties = HostUtil.readProperties(filename);
			properties.put(key, value);
			HostUtil.saveProperties(properties, filename);
		}catch(Throwable e){
			throw new RuntimeException(e.getMessage());
		}finally{
		}
	}

	public String getDefaultHostKey(){
		try{
			String hostKey = this.getProperty(HostProperties.HOST_KEY);
			if(hostKey == null || hostKey.isEmpty() ){
				hostKey = UUID.randomUUID().toString()+"-"+System.currentTimeMillis();
				this.saveProperty("hostKey", hostKey);
			}
			
			return hostKey;
		}catch(Throwable e){
			throw new RuntimeException(e.getMessage());
		}finally{
		}
	}

	public String getDefaultHostAddress() {
		try {
			try {
				String hostAddr = this.getProperty(HostProperties.HOST_ADDR);
				if (hostAddr != null && !hostAddr.isEmpty()
						&& hostAddr.indexOf(":") > 0) {
					String str[] = hostAddr.split(":");
					if (!HostUtil.isPortUsing(str[0], Integer.parseInt(str[1]))) {
						return hostAddr;
					}
				}
			} catch (Throwable e) {

			}

			// 其它情况则保存
			String strAddr = HostUtil.getDefaultHostIp() + ":"+ HostUtil.getDefaultHostPort();
			this.saveProperty("hostAddr", strAddr);

			return strAddr;
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage());
		} finally {
		}
	}
}
