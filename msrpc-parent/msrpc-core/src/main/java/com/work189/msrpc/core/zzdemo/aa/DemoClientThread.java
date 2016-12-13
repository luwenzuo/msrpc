package com.work189.msrpc.core.zzdemo.aa;

import org.springframework.context.ApplicationContext;

import com.work189.msrpc.core.transport.channel.worker.WorkerCount;
import com.work189.msrpc.core.zzdemo.service.LoginService;
import com.work189.msrpc.core.zzdemo.service.entity.LoginRequest;
import com.work189.msrpc.core.zzdemo.service.entity.LoginResponse;

public class DemoClientThread extends Thread {

	protected static WorkerCount workerCount = new WorkerCount("client");
	protected ApplicationContext ac;
	protected static LoginService loginService;
	
	public DemoClientThread(ApplicationContext ac){
		this.ac = ac;
	}

    @Override
    public void run() {
    	System.out.println("线程启动");
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
	public void test(){
		if(loginService == null){
			loginService = (LoginService) ac.getBean("loginService");
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
