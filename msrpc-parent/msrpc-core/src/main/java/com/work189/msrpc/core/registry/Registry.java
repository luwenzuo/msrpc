package com.work189.msrpc.core.registry;

import com.work189.msrpc.core.rpc.proxy.bean.ProxyBeanDefine;

public interface Registry {
    public void register(ProxyBeanDefine bean);
    public void unregister(ProxyBeanDefine bean);
    public void subscribe(ProxyBeanDefine bean);
    public void unsubscribe(ProxyBeanDefine bean);
    public void lookup(ProxyBeanDefine bean);
}
