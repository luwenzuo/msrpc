package com.work189.msrpc.core.registry.support.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import com.work189.msrpc.core.common.log.Logger;
import com.work189.msrpc.core.common.log.LoggerFactory;
import com.work189.msrpc.core.config.properties.property.PropertyMsrpcDefine;
import com.work189.msrpc.core.rpc.proxy.bean.ProxyBeanDefine;

public class MysqlDBDao {
	protected Logger logger = LoggerFactory.getLogger(MysqlDBDao.class);
	private static Connection connection;

	public synchronized Connection getConnection() {
		try{
			if(connection != null && !connection.isClosed()){
				connection.close();
			}
		}catch(Throwable e){
			
		}
		
		String url = PropertyMsrpcDefine.msrpc_registry_url.toString();
		String username = PropertyMsrpcDefine.msrpc_registry_username.toString();
		String password = PropertyMsrpcDefine.msrpc_registry_password.toString();

		try {
			
			connection = DriverManager.getConnection(url, username, password);
			return connection;
		} catch (Throwable e) {/*
			logger.error(
					 "msrpc_registry_url="+PropertyMsrpcDefine.msrpc_registry_url.toString()
					+";msrpc_registry_username="+PropertyMsrpcDefine.msrpc_registry_username.toString()
					+";msrpc_registry_driver="+PropertyMsrpcDefine.msrpc_registry_driver.toString());*/
			throw new RuntimeException(e);
		}
	}
	
	public static void closeConnection(){
		try{
			if(connection != null && !connection.isClosed()){
				connection.close();
			}
		}catch(Throwable e){
			
		}
		connection = null;
	}
	
	public Map<String,String> getParamsMap(ProxyBeanDefine bean){

		Map<String,String> params = new HashMap<String, String>();
		params.put("service_id", bean.getServiceId());
		params.put("pc_flag", bean.getPcFlag());
		params.put("interface_class", bean.getInterfaceName());
		params.put("service_version", bean.getVersion());
		params.put("service_group", bean.getGroup());
		params.put("service_host_ip", bean.getServiceHostIp());
		params.put("service_host_port", String.valueOf(bean.getServiceHostPort()) );
		params.put("service_host_key", bean.getServiceHostKey());
		params.put("host_key", bean.getHostKey());
		params.put("host_ip", bean.getHostIp());
		params.put("host_port", String.valueOf(bean.getHostPort()) );
		params.put("active_key", bean.getActiveKey());
		return params;
	}
	
	public ProxyBeanDefine getParamsBean(Map<String,String> params){
		ProxyBeanDefine bean = new ProxyBeanDefine();
		
		bean.setActiveKey( params.get("active_key") );
		bean.setGroup( params.get("service_group") );
		bean.setHostIp( params.get("host_ip") );
		bean.setHostKey( params.get("host_key") );
		bean.setPcFlag( params.get("pc_flag") );
		bean.setServiceHostIp( params.get("service_host_ip") );
		bean.setServiceHostKey( params.get("service_host_key") );
		bean.setServiceId( params.get("service_id") );
		bean.setInterfaceName( params.get("interface_class") );
		bean.setVersion( params.get("service_version") );
		bean.setHostPort( Integer.parseInt(params.get("host_port")) );
		bean.setServiceHostPort( Integer.parseInt(params.get("service_host_port")) );
		
		return bean;
	}
}
