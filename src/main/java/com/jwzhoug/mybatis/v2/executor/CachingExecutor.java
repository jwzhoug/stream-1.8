package com.jwzhoug.mybatis.v2.executor;

public class CachingExecutor implements Executor {
    public CachingExecutor(SimpleExecutor simpleExecutor) {

    }

    @Override
    public <T> T query(String statement, Object[] parameter, Class pojo) {
        return null;
    }
}
