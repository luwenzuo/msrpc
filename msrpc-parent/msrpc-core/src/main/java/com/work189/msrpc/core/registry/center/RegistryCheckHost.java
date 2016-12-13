package com.work189.msrpc.core.registry.center;

import java.util.Map.Entry;

import com.work189.msrpc.core.registry.RegistryCenter;
import com.work189.msrpc.core.registry.entity.RegistryHostEntity;
import com.work189.msrpc.core.registry.support.mysql.MysqlRegistryHostDao;
import com.work189.msrpc.core.rpc.exchange.host.DefaultExchangeHostConfig;

public class RegistryCheckHost {

	public static synchronized void checkProviderHost() {
		try {
			RegistryCheckService.checkAllProviderServices();

			MysqlRegistryHostDao dao = new MysqlRegistryHostDao();
			dao.deleteNotActiveKey(dao.getConnection(),
					DefaultExchangeHostConfig.config.getHostKey(),
					DefaultExchangeHostConfig.config.getActiveKey());
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static synchronized void activeProviderHost() {
		for (	Entry<String, RegistryHostEntity> item : 
				RegistryCenter.master.getHosts().entrySet()) {
			try {
				RegistryHostEntity host = item.getValue();
				MysqlRegistryHostDao dao = new MysqlRegistryHostDao();
				dao.updateHostClient(dao.getConnection(),
						host.getHostKey(),
						host.getActiveKey(),
						host.getServer().getChannels().size());
			} catch (Throwable e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
	}

}
