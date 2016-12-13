package com.work189.msrpc.core.zzdemo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.work189.msrpc.core.rpc.protocol.ProtocolManager;
import com.work189.msrpc.core.zzdemo.service.entity.LoginRequest;

public class TT {

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		long time = 0;
		long add=0;

		Map<Integer, Object> maps = new ConcurrentHashMap();
		
		LoginRequest request = new LoginRequest();
		request.setUsername("db");
		request.setPassword("123456");
		request.setData( null );
		for(int i=1; i<Integer.MAX_VALUE; i++){
			
			ProtocolManager.getSerializer().serialize(request);
			
			add ++;
			
			if(System.currentTimeMillis()-time > 5*1000){
				System.out.println("add="+add);
				time = System.currentTimeMillis();
				add = 0;
			}
		}
	}

}
