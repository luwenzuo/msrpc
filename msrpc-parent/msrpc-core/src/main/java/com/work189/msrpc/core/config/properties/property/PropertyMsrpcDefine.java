package com.work189.msrpc.core.config.properties.property;


public class PropertyMsrpcDefine{

	//服务注册中心
	public static PropertyField msrpc_registry_url			= new PropertyField("msrpc.registry.url", PropertyMsrpcDefine.class);
	public static PropertyField msrpc_registry_username		= new PropertyField("msrpc.registry.username", PropertyMsrpcDefine.class);
	public static PropertyField msrpc_registry_password		= new PropertyField("msrpc.registry.password", PropertyMsrpcDefine.class);
	public static PropertyField msrpc_registry_driver		= new PropertyField("msrpc.registry.driver", PropertyMsrpcDefine.class);
	//日志框架
	public static PropertyField msrpc_logapi				= new PropertyField("msrpc.logapi", PropertyMsrpcDefine.class);
	//服务器参数
	public static PropertyField msrpc_server_queue		= new PropertyField("msrpc.server.queue", PropertyMsrpcDefine.class, "1000");
	public static PropertyField msrpc_server_init		= new PropertyField("msrpc.server.init", PropertyMsrpcDefine.class, "5");
	public static PropertyField msrpc_server_max		= new PropertyField("msrpc.server.max", PropertyMsrpcDefine.class, "600");
	public static PropertyField msrpc_server_await		= new PropertyField("msrpc.server.await", PropertyMsrpcDefine.class, "10000");
	//客户端参数
	public static PropertyField msrpc_client_await		= new PropertyField("msrpc.client.await", PropertyMsrpcDefine.class, "10000");
}
