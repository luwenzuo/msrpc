package com.work189.msrpc.core.transport.channel.worker;

import java.util.concurrent.atomic.AtomicLong;

public class WorkerCount {

	private long printTime = 5*1000;
	private long m_begin_time=0;
	private AtomicLong m_add_count = new AtomicLong(0);
	private AtomicLong m_total_count = new AtomicLong(0);
	private String workerName;
	
	public WorkerCount(String name){
		this.workerName = name;
	}
	
	public void add(){
		m_add_count.getAndIncrement();
		m_total_count.getAndIncrement();
	}

	public void addAndPrint(){
		add();

		if (System.currentTimeMillis() - m_begin_time > printTime) {
			synchronized (m_add_count) {
				if (m_add_count.get() > 1) {
					double qps = m_add_count.get();
					qps = qps / (System.currentTimeMillis() - m_begin_time);
					qps = qps * 1000;
					long lqps = (long) qps;
					int threadCount = Thread.currentThread().getThreadGroup().activeCount();
					System.out.println(this.workerName + "-->thread="
							+ threadCount + ";total=" + m_total_count + ";add="
							+ m_add_count + ";QPS=" + lqps + "");
					m_add_count.set(0);
					m_begin_time = System.currentTimeMillis();
				}
			}
		}
	}
}
