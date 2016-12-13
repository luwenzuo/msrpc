package com.work189.msrpc.core.registry.support.mysql;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.work189.msrpc.core.common.exception.RegistryException;
import com.work189.msrpc.core.registry.support.AbstractRegistry;
import com.work189.msrpc.core.rpc.exchange.ExchangeManager;
import com.work189.msrpc.core.rpc.exchange.host.ExchangeHostHander;
import com.work189.msrpc.core.rpc.proxy.bean.ProxyBeanDefine;
import com.work189.msrpc.core.transport.channel.support.DefaultChannelHanderClient;

public class MysqlRegistrySupport extends AbstractRegistry{

	public Connection getConnection(){
		return null;
	}

	public void register(ProxyBeanDefine bean) {
		if(bean == null){
			return;
		}
		
		if(!"provider".equals(bean.getPcFlag())){
			throw new RuntimeException("服务标识错误"+bean.getServiceId());
		}
		
		//检查主机
		checkHost(bean);
		
		// 更新主机最新状态
		try {
			MysqlRegistryHostDao hostDao = new MysqlRegistryHostDao();
			hostDao.updateHostActiveKey(hostDao.getConnection(),bean.getHostKey(), bean.getActiveKey());
			
			if(isServiceEmpty()){
				//首次初始化,清空
				hostDao.deleteNotActiveKey(hostDao.getConnection(), bean.getHostKey(), bean.getActiveKey());
			}
		} catch (Throwable e) {

		}
		
		//限制服务发布权限
		{
			MysqlRegistryPermissionDao permissionDao = new MysqlRegistryPermissionDao();
			if(!permissionDao.isCanRegisterPermission(permissionDao.getConnection(), 
					bean.getGroup(), bean.getHostIp()) ){
				System.err.println("服务分组["+bean.getGroup()+"]已经限制指定主机才能发布该组服务");
				return;
			}
		}
		

		//key: service_id+pcflag+host_key
		//service_id|pcflag|interface_class|version|group|host_key|host_ip|host_port|last_date|
		try{
			MysqlRegistryServiceDao dao = new MysqlRegistryServiceDao();
			dao.deleteService(dao.getConnection(), bean.getServiceId(), bean.getHostKey(), "provider");

			Map<String,String> params = dao.getParamsMap(bean);
			dao.insertService(dao.getConnection(), params);
		}catch(Throwable e){
			e.printStackTrace();
			throw new RegistryException("服务注册失败"+bean.getServiceId());
		}

		super.register(bean);
	}

	public void unregister(ProxyBeanDefine bean) {
		if(bean == null){
			return;
		}
		
		try{
			MysqlRegistryServiceDao dao = new MysqlRegistryServiceDao();
			dao.deleteService(dao.getConnection(), bean.getServiceId(), bean.getHostKey(), "consumer");
		}catch(Throwable e){
			e.printStackTrace();
		}
		super.unregister(bean);
	}

	public void subscribe(ProxyBeanDefine bean) {
		if(bean == null){
			return;
		}
		
		if(!"consumer".equals(bean.getPcFlag())){
			throw new RuntimeException("服务标识错误"+bean.getServiceId());
		}
		
		if(bean.isOk()){
			//服务已订阅
			return;
		}

		//检查主机
		checkHost(bean);
		
		// 更新主机最新状态
		try {
			MysqlRegistryHostDao hostDao = new MysqlRegistryHostDao();
			if(isServiceEmpty()){
				//首次初始化,清空
				hostDao.deleteNotActiveKey(hostDao.getConnection(), bean.getHostKey(), bean.getActiveKey());
			}
		} catch (Throwable e) {

		}

		// 限制服务发布权限
		{
			MysqlRegistryPermissionDao permissionDao = new MysqlRegistryPermissionDao();
			if (!permissionDao.isCansubscribePermission(
					permissionDao.getConnection(), bean.getGroup(), bean.getHostIp())) {
				System.err.println("服务分组[" + bean.getGroup() + "]已经限制指定主机才能订阅该组服务");
				return;
			}
		}
		
		try{
			MysqlRegistryServiceDao dao = new MysqlRegistryServiceDao();
			
			Map<String,String> retParams = dao.selectProvider(dao.getConnection(), bean.getServiceId());
			if(retParams == null){
				throw new RegistryException("订阅服务不存在"+bean.getServiceId());
			}
			ProxyBeanDefine providerBean = dao.getParamsBean(retParams);
			
			dao.deleteService(dao.getConnection(), bean.getServiceId(), bean.getHostKey(), "consumer");
			
			//远程服务
			bean.setServiceHostIp( providerBean.getHostIp() );
			bean.setServiceHostKey( providerBean.getHostKey() );
			bean.setServiceHostPort( providerBean.getHostPort() );
			
			Map<String,String> regParams = dao.getParamsMap(bean);
			dao.insertService(dao.getConnection(), regParams);
		}catch(Throwable e){
		}
		super.subscribe(bean);
	}

	public void unsubscribe(ProxyBeanDefine bean) {
		if(bean == null){
			return;
		}
		try{
			MysqlRegistryServiceDao dao = new MysqlRegistryServiceDao();
			dao.deleteService(dao.getConnection(), bean.getServiceId(), bean.getHostKey(), bean.getPcFlag());
		}catch(Throwable e){
			e.printStackTrace();
		}
		super.unsubscribe(bean);
	}
	
	@Override
	public void lookup(ProxyBeanDefine bean) {

		if(bean == null){
			return;
		}
		
		if(!"consumer".equals(bean.getPcFlag())){
			throw new RuntimeException("服务标识错误"+bean.getServiceId());
		}

		//检查主机
		checkHost(bean);
		
		try{
			MysqlRegistryServiceDao dao = new MysqlRegistryServiceDao();
			List< Map<String,String> > retParamsList = dao.selectProviderLists(dao.getConnection(), bean.getServiceId());
			if(retParamsList == null){
				throw new RegistryException("订阅服务不存在"+bean.getServiceId());
			}
			
			Map<String,String> retParams = null;
			for(Map<String,String> item:retParamsList){
				try{
					//检查服务是否可用
					DefaultChannelHanderClient channelHander = new DefaultChannelHanderClient();
					ExchangeHostHander host = new ExchangeHostHander();
					host.setHostAddress(item.get("host_ip"), Integer.parseInt(item.get("host_port")) );
					ExchangeManager.getClient(host, channelHander).getChannel().toString();
					retParams = item;
					break;
				}catch(Throwable e){
					//无效服务,继续查询下一下服务
					nodifyHostError( item.get("host_key") );
				}
			}
			if(retParams == null){
				throw new RegistryException("订阅服务不存在"+bean.getServiceId());
			}
			
			ProxyBeanDefine providerBean = dao.getParamsBean(retParams);
			dao.deleteService(dao.getConnection(), bean.getServiceId(), bean.getHostKey(), "consumer");
			
			//远程服务主机
			bean.setServiceHostIp( providerBean.getHostIp() );
			bean.setServiceHostKey( providerBean.getHostKey() );
			bean.setServiceHostPort( providerBean.getHostPort() );
			//登记服务信息
			Map<String,String> regParams = dao.getParamsMap(bean);
			dao.insertService(dao.getConnection(), regParams);
		}catch(Throwable e){
			//e.printStackTrace();
		}
		
		super.subscribe(bean);
	}

	public void checkHost(ProxyBeanDefine bean) {
		try {
			MysqlRegistryHostDao dao = new MysqlRegistryHostDao();
			Map<String, String> params = dao.getHostInfo(dao.getConnection(),bean.getHostKey());
			if (params == null) {
				params = new HashMap<String, String>();
				params.put("host_key", bean.getHostKey());
				params.put("host_ip", bean.getHostIp());
				params.put("host_port", String.valueOf(bean.getHostPort()));
				params.put("active_key", bean.getActiveKey());
				params.put("uc_group", bean.getGroup());
				dao.insertHostKey(dao.getConnection(), params);
				return;
			}

			String key1 = params.get("active_key");
			String key2 = bean.getActiveKey();
			if (!key1.equals(key2)) {
				dao.updateHostActiveKey(
						dao.getConnection(), 
						bean.getHostKey(),
						bean.getActiveKey());
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void nodifyHostError(String hostKey) {

		try {
			MysqlRegistryHostDao dao = new MysqlRegistryHostDao();
			dao.addHostLastError(dao.getConnection(), hostKey);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
