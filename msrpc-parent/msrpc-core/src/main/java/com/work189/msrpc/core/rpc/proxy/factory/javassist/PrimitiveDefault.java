package com.work189.msrpc.core.rpc.proxy.factory.javassist;

import java.util.HashMap;
import java.util.Map;

public class PrimitiveDefault {
	
	private static Map<Class<?>, String> primitiveValues = new HashMap<Class<?>, String>();

	static {
		primitiveValues.put(boolean.class, "false");
		primitiveValues.put(char.class, "0");
		primitiveValues.put(byte.class, "0");
		primitiveValues.put(short.class, "0");
		primitiveValues.put(int.class, "0");
		primitiveValues.put(long.class, "0L");
		primitiveValues.put(float.class, "0.00");
		primitiveValues.put(double.class, "0.00");
		primitiveValues.put(void.class, "");
	}
	
	public static Object getDefaultReturnValue(Class<?> returnType) {
		if (returnType != null && returnType.isPrimitive()) {
			return primitiveValues.get(returnType);
		}
		return null;
	}
}
