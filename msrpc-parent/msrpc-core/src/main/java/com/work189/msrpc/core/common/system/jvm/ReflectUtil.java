package com.work189.msrpc.core.common.system.jvm;

import java.lang.reflect.Field;

public class ReflectUtil {

	public static Object getFieldObject(Object classObject, String fieldname){
		try {
			Class<?> clazz = classObject.getClass();
			Field field = clazz.getDeclaredField(fieldname);
			field.setAccessible(true);
			return field.get(classObject);
		}catch (Throwable e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
