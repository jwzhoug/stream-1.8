package com.jwzhoug.mybatis.v2.plugin;

import com.jwzhoug.mybatis.v2.executor.Executor;

public class InterceptorChain {
    public void addInterceptor(Interceptor interceptor) {


    }

    public boolean hasPlugin() {
        return false;
    }

    public Object pluginAll(Executor executor) {
        return null;
    }
}
