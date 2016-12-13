package com.work189.msrpc.core.registry.support.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlRegistryServiceDao extends MysqlDBDao{
	
	public Map<String,String> selectService(Connection connection, String serviceId, String pcflag) throws SQLException{
		String sql = "select service_id,pc_flag,interface_class, service_version,service_group,"
				+ " service_host_ip,service_host_port,service_host_key,"
				+ " host_key,host_ip,host_port,last_date,active_key "
				+ " from S10.registry_service "
				+ " where service_id=? and pc_flag=? order by last_date desc limit 1";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)){
			int i=1;
			pstmt.setString(i++, serviceId );
			pstmt.setString(i++, pcflag );
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
	
	public Map<String,String> selectProvider(Connection connection, String serviceId ) throws SQLException{
		String sql = "select registry_service.* from registry_host"
				+ " INNER JOIN registry_service ON(registry_host.host_key=registry_service.host_key)"
				+ " where registry_service.service_id=?"
				+ " and UNIX_TIMESTAMP(registry_host.last_date)>UNIX_TIMESTAMP(CURRENT_TIMESTAMP())-30"
				+ " and registry_service.pc_flag='provider' order by registry_host.last_error asc, registry_host.clients asc";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)){
			int i=1;
			pstmt.setString(i++, serviceId );
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
	
	public List< Map<String,String> > selectProviderLists(Connection connection, String serviceId) throws SQLException{
		List<Map<String,String>> providerLists = new ArrayList<>();
		String sql = "select registry_service.* from registry_host"
				+ " INNER JOIN registry_service ON(registry_host.host_key=registry_service.host_key)"
				+ " where registry_service.service_id=?"
				+ " and registry_service.pc_flag='provider' order by registry_host.last_error asc, registry_host.clients asc";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)){
			int i=1;
			pstmt.setString(i++, serviceId );
			try(ResultSet rs = pstmt.executeQuery() ){
				while(rs.next()){
					Map<String,String> params = new HashMap<String, String>();
					ResultSetMetaData rsmd = pstmt.getMetaData();
					for(int k=1; k<rsmd.getColumnCount(); k++){
						String name = rsmd.getColumnName(k);
						params.put(name, rs.getString(k) );
					}
					providerLists.add(params);
				}
			}
		}
		return providerLists;
	}
	
	public int deleteService(Connection connection, String serviceId, String hostKey, String pcFlag) throws SQLException{
		String sql = "delete from S10.registry_service "
				+ " where service_id=? and host_key=? and pc_flag=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)){
			int i=1;
			pstmt.setString(i++, serviceId);
			pstmt.setString(i++, hostKey);
			pstmt.setString(i++, pcFlag);
			return pstmt.executeUpdate();
		}
	}
	
	public void insertService(Connection connection, Map<String,String> params) throws SQLException {
		//key: service_id+pcflag+host_key
		//service_id|pcflag|interface_class|version|group|host_key|host_ip|host_port|last_date|
		String sql = "insert into S10.registry_service set service_id=?,pc_flag=?,interface_class=?,"
				+ " service_version=?,service_group=?,service_host_ip=?,service_host_port=?,service_host_key=?,"
				+ " host_key=?,host_ip=?,host_port=?,active_key=?,last_date=CURRENT_TIMESTAMP()";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)){
			int i=1;
			pstmt.setString(i++, params.get("service_id"));
			pstmt.setString(i++, params.get("pc_flag"));
			pstmt.setString(i++, params.get("interface_class"));
			pstmt.setString(i++, params.get("service_version"));
			pstmt.setString(i++, params.get("service_group"));
			pstmt.setString(i++, params.get("service_host_ip"));
			pstmt.setString(i++, params.get("service_host_port"));
			pstmt.setString(i++, params.get("service_host_key"));
			pstmt.setString(i++, params.get("host_key"));
			pstmt.setString(i++, params.get("host_ip"));
			pstmt.setString(i++, params.get("host_port"));
			pstmt.setString(i++, params.get("active_key"));
			pstmt.executeUpdate();
		}
	}
}
