package com.work189.msrpc.core.rpc.protocol.message.impl;

public class MessageConstant {
	//RSF版本号
	public final static int SYS_VERSION = 11;
	//数据编码
	public final static int SYS_CHARSET = 12;
	//系统请求流水
	public final static int SYS_REQUEST_ID = 13;
	//系统请求时间
	public final static int SYS_REQUEST_TIME = 14;
	//应用标识ID
	public final static int SYS_APP_ID = 15;
	//服务器节点
	public final static int SYS_HOST_NODE = 16;
	//服务器IP
	public final static int SYS_HOST_IP = 17;
	//服务器安全检查
	public final static int SYS_HOST_SECRET = 18;
	//业务交易代码
	public final static int SERVICE_ID = 50;
	//业务安全模式(md5,des,rsa)
	public final static int SERVICE_SECURITY_MODE = 51;
	//业务安全密钥
	public final static int SERVICE_SECURITY_KEY = 52;
	//业务交易数据包
	public final static int SERVICE_BUFFER_DATA = 53;
}
