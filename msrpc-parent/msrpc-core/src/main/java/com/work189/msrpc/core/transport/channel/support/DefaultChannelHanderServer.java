package com.work189.msrpc.core.transport.channel.support;

import com.work189.msrpc.core.registry.center.RegistryCheckHost;
import com.work189.msrpc.core.transport.channel.Channel;
import com.work189.msrpc.core.transport.channel.worker.WorkerDispatch;

public class DefaultChannelHanderServer extends AbstractChannelHandler {

	@Override
	public synchronized void received(Channel channel, Object message) {
		WorkerDispatch.addTask(channel, message);
		
		/*DefaultExchangeServerHander hander = new DefaultExchangeServerHander();
		hander.setRequestData((RpcBuffer) message);
		hander.callService();
		RpcBuffer retBuffer = hander.getResponseData();
		channel.send(retBuffer, true);*/
	}
	
	@Override
	public void connected(Channel channel) {
		//更新服务器连接数
		RegistryCheckHost.activeProviderHost();
	}
}
