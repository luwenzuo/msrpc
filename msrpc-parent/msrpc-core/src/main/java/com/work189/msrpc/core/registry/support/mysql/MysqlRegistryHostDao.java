package com.work189.msrpc.core.registry.support.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MysqlRegistryHostDao  extends MysqlDBDao{

	public void insertHostKey(Connection connection, Map<String,String> params) throws SQLException{
		String sql = "insert into registry_host set host_key=?,host_ip=?,host_port=?,"
				+ " uc_group=?,active_key=?,last_date=CURRENT_TIMESTAMP() "
				+ " ON DUPLICATE KEY UPDATE host_ip=VALUES(host_ip),host_port=VALUES(host_port),"
				+ " uc_group=VALUES(uc_group),active_key=VALUES(active_key),last_date=VALUES(last_date)";
		try(PreparedStatement pstmt = connection.prepareStatement(sql)){
			int i=1;
			pstmt.setString(i++, params.get("host_key"));
			pstmt.setString(i++, params.get("host_ip"));
			pstmt.setString(i++, params.get("host_port"));
			pstmt.setString(i++, params.get("uc_group"));
			pstmt.setString(i++, params.get("active_key"));
			pstmt.executeUpdate();
		}
	}

	public Map<String,String> getHostInfo(Connection connection, String hostKey) throws SQLException{
		String sql = "select * from registry_host where host_key=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)){
			pstmt.setString(1, hostKey );
			try(ResultSet rs = pstmt.executeQuery() ){
				if(rs.next()){
					Map<String,String> params = new HashMap<String, String>();
					ResultSetMetaData rsmd = pstmt.getMetaData();
					for(int k=1; k<rsmd.getColumnCount(); k++){
						String name = rsmd.getColumnName(k);
						params.put(name, rs.getString(k) );
					}
					return params;
				}
			}
		}
		return null;
	}
	
	public void updateHostActiveKey(Connection connection, String hostKey, String activeKey) throws SQLException{
		String sql = "update registry_host set active_key=?,last_date=CURRENT_TIMESTAMP(),last_error=0 where host_key=?";
		try( PreparedStatement pstmt = connection.prepareStatement(sql) ){
			pstmt.setString(1, activeKey);
			pstmt.setString(2, hostKey);
			pstmt.executeUpdate();
		}
	}
	
	public void addHostLastError(Connection connection, String hostKey) throws SQLException{
		String sql = "update registry_host set last_error=last_error+1 where host_key=?";
		try( PreparedStatement pstmt = connection.prepareStatement(sql) ){
			pstmt.setString(1, hostKey);
			pstmt.executeUpdate();
		}
	}
	
	public void updateHostClient(Connection connection, String hostKey, String activeKey, int clients) throws SQLException{
		String sql = "update registry_host set active_key=?,last_date=CURRENT_TIMESTAMP(),clients=? where host_key=?";
		try( PreparedStatement pstmt = connection.prepareStatement(sql) ){
			pstmt.setString(1, activeKey);
			pstmt.setInt(2, clients);
			pstmt.setString(3, hostKey);
			pstmt.executeUpdate();
		}
	}
	
	public void deleteNotActiveKey(Connection connection, String hostKey, String activeKey) throws SQLException{
		String sql = "delete from registry_service where host_key=? and active_key!=?";
		try(PreparedStatement pstmt = connection.prepareStatement(sql)){
			pstmt.setString(1, hostKey);
			pstmt.setString(2, activeKey);
			pstmt.executeUpdate();
		}
	}
	
	public void deleteHostService(Connection connection, String hostKey) throws SQLException{
		String sql = "delete from registry_service where host_key=? ";
		try(PreparedStatement pstmt = connection.prepareStatement(sql)){
			pstmt.setString(1, hostKey);
			pstmt.executeUpdate();
		}
	}
	
	public void deleteNotHostKey(Connection connection) throws SQLException{
		String sql="delete from registry_service where host_key not in("
				+ "select host_key from registry_host)";
		try(PreparedStatement pstmt = connection.prepareStatement(sql)){
			pstmt.executeUpdate();
		}
	}
}
