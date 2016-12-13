package com.work189.msrpc.core.zzdemo.xx;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

public class MinaChannel {

	public final static ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<>(2000);
	
	public static Worker worker=null;

	public static int add_count=0;
	public void addChannel(IoSession ioSession){
		try {
			startWorker();
			if(queue.size() > 100){
				return;
			}
			queue.offer(ioSession, 100, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
	}
	
	public void startWorker(){
		synchronized (MinaChannel.class) {
			if(worker == null){
				worker = new Worker();
				worker.start();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		//BlockingQueue bq = new BlockingQueue();
		
		for(int i=0;i<100; i++){
			System.out.println("i="+i);
			//queue.poll(100, TimeUnit.SECONDS);
			queue.offer(i, 100, TimeUnit.SECONDS);
			System.out.println(queue.size());
		}
	}
	
	public class Worker extends Thread{
		
		@SuppressWarnings("unused")
		private int count = 0;
		public void run(){
			System.out.println("worker runing...");
			while(true){
				try{
					//Thread.sleep(1*1000);
					IoSession ioSession = (IoSession) queue.poll(100, TimeUnit.SECONDS);

					if(ioSession != null){
						//System.out.println("send---begin---");
						//IoBuffer ioBuffer = IoBuffer.allocate(4*1024*1024);
						//System.out.println("发送");
						byte []data = new byte[256];
						IoBuffer ioBuffer = IoBuffer.wrap(data);
						ioSession.write(ioBuffer).awaitUninterruptibly();
						//System.out.println("send---end---"+count++);
					}else{
						System.out.println("session null");
					}
				}catch(Throwable e){
					e.printStackTrace();
				}
			}
		}
	}
}
