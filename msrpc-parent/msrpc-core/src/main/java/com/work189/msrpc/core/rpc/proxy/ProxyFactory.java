package com.work189.msrpc.core.rpc.proxy;

import com.work189.msrpc.core.rpc.proxy.bean.ProxyBeanDefine;

public interface ProxyFactory {

	public void export(ProxyBeanDefine proxyBeanDefine);
	public Object refer(ProxyBeanDefine proxyBeanDefine);
}
