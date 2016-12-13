package com.work189.msrpc.core.rpc.exchange.buffer;

public class ExchangeResponse {
	private Object responseObject;
	private Object throwableObject;
	private String throwableMessage;

	public Object getResponseObject() {
		return responseObject;
	}
	public void setResponseObject(Object responseObject) {
		this.responseObject = responseObject;
	}
	public Object getThrowableObject() {
		return throwableObject;
	}
	public void setThrowableObject(Object throwableObject) {
		this.throwableObject = throwableObject;
	}
	public String getThrowableMessage() {
		return throwableMessage;
	}
	public void setThrowableMessage(String throwableMessage) {
		this.throwableMessage = throwableMessage;
	}
	
	
}
