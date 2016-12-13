package com.work189.msrpc.demo.consumer.test;

import org.springframework.beans.factory.annotation.Autowired;

import com.work189.msrpc.core.transport.channel.worker.WorkerCount;
import com.work189.msrpc.demo.service.LoginService;
import com.work189.msrpc.demo.service.entity.LoginRequest;
import com.work189.msrpc.demo.service.entity.LoginResponse;

public class TestProgress {
	protected static WorkerCount workerCount = new WorkerCount("client");
	protected static TestThread testThread;

	@Autowired
	public LoginService loginService;
	
	public TestProgress(){
		System.out.println("TestProgress");
		testThread = new TestThread();
		testThread.start();
	}
	
	public class TestThread extends Thread{
		
		public void run(){
			loadTest();
		}
	    
	    public void loadTest(){
	    	try{
	    		while(true){
	    			try{
	    				test();
	    			}catch(Throwable e){
	    				e.printStackTrace();
	    				Thread.sleep(1*1000);
	    			}
	    		}
	    	}catch(Throwable e){
	    		e.printStackTrace();
	    	}
	    }
	    
		@SuppressWarnings("unused")
		public void test() throws Exception{
			if(loginService == null){
				Thread.sleep(1000);
				return;
			}

			LoginRequest request = new LoginRequest();
			request.setUsername("db");
			request.setPassword("123456");
			request.setData( null );
			LoginResponse response = loginService.login(request);
			//System.out.println(response.getErrcode());

			//统计
			workerCount.addAndPrint();
		}
	}
}
