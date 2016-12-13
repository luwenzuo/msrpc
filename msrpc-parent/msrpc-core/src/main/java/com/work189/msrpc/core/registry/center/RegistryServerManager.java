package com.work189.msrpc.core.registry.center;

import com.work189.msrpc.core.registry.center.RegistryServer;
import com.work189.msrpc.core.registry.support.mysql.MysqlRegistryHostDao;
import com.work189.msrpc.core.rpc.exchange.host.DefaultExchangeHostConfig;

public class RegistryServerManager {
	private static RegistryServer registryServer;
	
	public static void initialize(){
		
		//注册中心服务监控
		registryServer = new RegistryServer();
		registryServer.start();
	}
	
	public static void destroy(){
		try{
			registryServer.stop();
			
			MysqlRegistryHostDao dao = new MysqlRegistryHostDao();
			dao.deleteHostService(dao.getConnection(), DefaultExchangeHostConfig.config.getHostKey() );
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	public static void clearAndExit(){
		try{
			MysqlRegistryHostDao dao = new MysqlRegistryHostDao();
			dao.deleteHostService(dao.getConnection(), DefaultExchangeHostConfig.config.getHostKey() );
		}catch(Throwable e){
			//e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}
