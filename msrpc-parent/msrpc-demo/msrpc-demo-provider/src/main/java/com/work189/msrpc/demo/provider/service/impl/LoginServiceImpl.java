package com.work189.msrpc.demo.provider.service.impl;

import java.util.Random;

import com.work189.msrpc.demo.service.LoginService;
import com.work189.msrpc.demo.service.entity.LoginRequest;
import com.work189.msrpc.demo.service.entity.LoginResponse;

public class LoginServiceImpl implements LoginService{

	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		
		//System.out.println(loginRequest.getUsername()+":"+loginRequest.getPassword());
		
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setErrcode("00000");
		loginResponse.setErrmsg("登录成功");
		
		try {
			int t = new Random().nextInt()%5;
			t = Math.abs(t)+1;
			//Thread.sleep(t*1);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		return loginResponse;
	}


}
