package com.work189.msrpc.core.common.exception;

public class MsrpcException extends RuntimeException{
	private static final long serialVersionUID = 3462359869615148095L;
	public MsrpcException (String errmsg){
		super(errmsg);
	}
}
