package com.work189.msrpc.core.zzdemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;



public class TestDB extends Thread{
	
	public static void testStart() {
		try {
			for (int i = 0; i < 20; i++) {
				TestDB testDb = new TestDB();
				testDb.start();
			}

			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		try{
			Connection connection = this.getConnection();
			connection.setAutoCommit(true);
			
			while(true){
				testInsert(connection);
			}
			
		}catch(Throwable e){
			e.printStackTrace();
		}
	}


	public Connection getConnection() throws SQLException{
		
		String url="jdbc:mysql://192.168.0.11:3306/S10?useUnicode=true&characterEncoding=UTF-8";
		String username="root";
		String password="123456";
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Throwable e) {
			throw new SQLException(e);
		}
		return DriverManager.getConnection(url, username, password);
	}

	public static long m_total_count=0;
	public static long m_add_count=0;
	public static long m_begin_time=0;
	public void testInsert(Connection connection) throws SQLException{
		try(PreparedStatement pstmt = connection.prepareStatement("insert into test.test_tt(f2,f3) values(?,?)")){
			pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			pstmt.setString(2, "bbbbbbbbbbbbbbbbbbbbbb555555555555555555555555555555");
			pstmt.executeUpdate();
		}
		//统计
		synchronized (TestDB.class) {
			m_total_count ++;
			m_add_count ++;
			
			//每5秒统计
			if(System.currentTimeMillis() - m_begin_time > 5*1000){
				int threadCount = Thread.currentThread().getThreadGroup().activeCount();
				System.out.println("threadCount="+threadCount+";total="+m_total_count+";add="+m_add_count);
				m_add_count = 0;
				m_begin_time=System.currentTimeMillis();
			}
		}
	}
	
	
}
