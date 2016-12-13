package com.work189.msrpc.core.rpc.proxy.factory.javassist;

import java.lang.reflect.InvocationHandler;

import com.work189.msrpc.core.rpc.proxy.factory.AbstractProxyFactory;

public class JavassistProxyFactory extends AbstractProxyFactory{

	@Override
	public Object getProxyObject(Class<?> interfaceClass, InvocationHandler invoker) {
		JavassistProxyObject javassistProxyObject = new JavassistProxyObject();
		Object object = javassistProxyObject.getProxyObject(interfaceClass, invoker);
		return object;
	}
}
