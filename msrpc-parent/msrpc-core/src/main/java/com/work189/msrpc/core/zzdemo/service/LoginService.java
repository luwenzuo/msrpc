package com.work189.msrpc.core.zzdemo.service;

import com.work189.msrpc.core.zzdemo.service.entity.LoginRequest;
import com.work189.msrpc.core.zzdemo.service.entity.LoginResponse;

public interface LoginService {

	LoginResponse login(LoginRequest loginRequest);
}
