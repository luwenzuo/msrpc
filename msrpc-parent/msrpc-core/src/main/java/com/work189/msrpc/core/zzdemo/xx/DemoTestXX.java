package com.work189.msrpc.core.zzdemo.xx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DemoTestXX {

	public static void mainTest(String[] args) {
		// TODO Auto-generated method stub
		/*if(true){
			new DemoTest().test();
		}*/
		
		//RedisUtil.startMoreThread();
		
		String ip="127.0.0.1";
		ip = args[2];

		List<Object> lists = new ArrayList<Object>();
		String user = args[0];
		int count = Integer.parseInt(args[1]);
		
		if("server".equals(user)){
			MinaServer server = new MinaServer(ip);
			lists.add(server);
		}
		if("client".equals(user)){
			for(int i=0; i<count; i++){
				MinaClient client = new MinaClient(ip);
				client.start();
				lists.add(client);
			}
		}
		DemoTestXX.waitTest();
	}
	
	public static void waitTest(){
		while(true){
			try {
				Thread.sleep(1*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	


	@SuppressWarnings("unused")
	public void test2() {
		byte[] data = new byte[4*1024*1024];
		//IoBuffer ioBuffer = IoBuffer.wrap(data);
		//Byte []dd = new Byte[4*1024*1024];
	}

	public static long m_total_count = 0;
	public static long m_add_count = 0;
	public static long m_begin_time = 0;

	@SuppressWarnings("unused")
	public void test() {
		// TODO Auto-generated method stub

		int count = 0;

		while (true) {
			test2();
			count++;

			// 统计
			//synchronized (DemoTest.class) {
				m_total_count++;
				m_add_count++;

				// 每5秒统计
				if (System.currentTimeMillis() - m_begin_time > 5 * 1000) {
					BigDecimal mspread = new BigDecimal((m_add_count*4*8)/1024);
					mspread = mspread.setScale(2);
/*	
					System.out.println(Runtime.getRuntime().totalMemory() + "|"
							+ Runtime.getRuntime().freeMemory() + "|"
							+ Runtime.getRuntime().maxMemory() + "|"
							+ Runtime.getRuntime().availableProcessors() + "|");*/
					int threadCount = 0;//Thread.currentThread().getThreadGroup().activeCount();
					System.out.println("-------------threadCount=" + threadCount + ";total="
							+ m_total_count + ";add=" + m_add_count+";spread="+mspread+"G");
					m_add_count = 0;
					m_begin_time = System.currentTimeMillis();
				}
			//}
		}
	}

}
