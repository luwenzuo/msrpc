package com.work189.msrpc.core.registry.support.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MysqlRegistryPermissionDao  extends MysqlDBDao{

	public boolean isCanRegisterPermission(Connection connection, String group, String hostIp) {
		try{
			boolean bret = true;
			String sql = "select host_ip from registry_permission where uc_group=? and uc_code='server_provider' ";
			try(PreparedStatement pstmt = connection.prepareStatement(sql)){
				int i=1;
				pstmt.setString(i++, group );
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()){
						bret = false;
						if(hostIp.equals( rs.getString(1)) ){
							return true;
						}
					}
				}
			}
			
			return bret;
		}catch(Throwable e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isCansubscribePermission(Connection connection, String group, String hostIp) {
		try{
			boolean bret = true;
			String sql = "select host_ip from registry_permission where uc_group=? and uc_code='server_consumer' ";
			try(PreparedStatement pstmt = connection.prepareStatement(sql)){
				int i=1;
				pstmt.setString(i++, group );
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()){
						bret = false;
						if(hostIp.equals( rs.getString(1)) ){
							return true;
						}
					}
				}
			}
			
			return bret;
		}catch(Throwable e){
			e.printStackTrace();
			return false;
		}
	}
}
