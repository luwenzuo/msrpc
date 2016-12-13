package com.work189.msrpc.core.rpc.proxy.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.work189.msrpc.core.rpc.proxy.bean.ProxyBeanDefine;

public abstract class AbstractProxyInvoker implements InvocationHandler{

	protected ProxyBeanDefine proxyBeanDefine;
	public AbstractProxyInvoker(ProxyBeanDefine proxyBeanDefine){
		this.proxyBeanDefine = proxyBeanDefine;
	}
	
	public abstract Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (method.getDeclaringClass() == Object.class) {
        	return method.invoke(proxy, args);
        }
        if ("toString".equals(methodName) && parameterTypes.length == 0) {
        	return this.proxyBeanDefine.getInterfaceName();
        }
        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
        	return this.hashCode();
        }
        if ("equals".equals(methodName) && parameterTypes.length == 1) {
        	return method.invoke(proxy, args);
        }
		return doInvoke(proxy, method, args);
	}

}
