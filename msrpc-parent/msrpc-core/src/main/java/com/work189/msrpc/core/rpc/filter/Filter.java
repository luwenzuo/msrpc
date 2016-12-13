package com.work189.msrpc.core.rpc.filter;

public interface Filter {

	public boolean filter(byte[] requestData);
}
