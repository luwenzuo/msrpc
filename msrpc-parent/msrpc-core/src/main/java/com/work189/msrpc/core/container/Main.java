package com.work189.msrpc.core.container;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.work189.msrpc.core.container.spring.SpringContainer;

public class Main {
	private static volatile boolean running = true;
	

	public static void main(String[] args) {
		runMain(args);
	}

	public static void runMain(String[] args) {

		try {
			Container container = new SpringContainer();
			container.start();
			System.out.println(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()) + " service server started!");

			synchronized (Main.class) {
				while (running) {
					try {
						Main.class.wait();
					} catch (Throwable e) {

					}
				}
			}
			
			container.stop();

		} catch (Throwable e) {
			e.printStackTrace();
		}

		System.out.println(Main.class.getName() + " server end");
		System.exit(0);
	}

}
