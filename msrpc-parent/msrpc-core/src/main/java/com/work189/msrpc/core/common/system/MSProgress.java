package com.work189.msrpc.core.common.system;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class MSProgress {

	public static int getProgressId() {
		try {
			RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
			String name = runtimeMXBean.getName();
			return Integer.parseInt(name.substring(0, name.indexOf('@')));
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
