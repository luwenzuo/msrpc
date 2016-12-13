package com.work189.msrpc.demo.service;

import com.work189.msrpc.demo.service.entity.LoginRequest;
import com.work189.msrpc.demo.service.entity.LoginResponse;


public interface LoginService {

	LoginResponse login(LoginRequest loginRequest);
}
