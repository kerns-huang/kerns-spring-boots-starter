/*
 * huiguan.com Inc.
 * Copyright (c) 2017 All Rights Reserved.
 */

package com.huiguan.boot.dubbo.listener;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.listener.ExporterListenerAdapter;
import com.huiguan.boot.dubbo.domain.ClassIdBean;
import com.huiguan.boot.dubbo.domain.SpringBootStarterDobboConstants;

import java.util.Set;

/**
 * dubbo 导出监听
 *
 * @author juyongcheng
 * @since $$Revision:1.0.0, $$Date: 2017/5/19 上午10:02 $$
 */
@Activate
public class ProviderExportListener extends ExporterListenerAdapter {
    // exported interfaces
    public static final Set<ClassIdBean> EXPORTEDINTERFACES_SET =
            new ConcurrentHashSet<ClassIdBean>();

    // exported urls
    public static final Set<URL> EXPORTED_URL = new ConcurrentHashSet<URL>();

    @Override
    public void exported(Exporter<?> exporter) throws RpcException {
        Invoker<?> invoker = exporter.getInvoker();
        Class<?> anInterface = invoker.getInterface();
        URL url = invoker.getUrl();
        String group = url.getParameter(SpringBootStarterDobboConstants.GROUP);
        String version = url.getParameter(SpringBootStarterDobboConstants.VERSION);
        ClassIdBean classIdBean = new ClassIdBean(anInterface, group, version);
        EXPORTEDINTERFACES_SET.add(classIdBean);
        if (!"injvm".equals(url.getProtocol())) {
            EXPORTED_URL.add(url);
        }
    }

    @Override
    public void unexported(Exporter<?> exporter) {
        Invoker<?> invoker = exporter.getInvoker();
        Class<?> anInterface = invoker.getInterface();
        URL url = invoker.getUrl();
        String group = url.getParameter(SpringBootStarterDobboConstants.GROUP);
        String version = url.getParameter(SpringBootStarterDobboConstants.VERSION);
        ClassIdBean classIdBean = new ClassIdBean(anInterface, group, version);
        EXPORTEDINTERFACES_SET.remove(classIdBean);
        if (!"injvm".equals(url.getProtocol())) {
            EXPORTED_URL.remove(url);
        }
    }
}
