package com.jwzhoug.mybatis.v2.session;

import com.jwzhoug.mybatis.v2.executor.Executor;

public class DefaultSqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        // 根据全局配置决定是否使用缓存装饰
        this.executor = configuration.newExecutor();
    }

    public Configuration getConfiguration(){
        return configuration;
    }

    public <T> T getMapper(Class<T> clazz){
        return configuration.getMapper(clazz,this);
    }

    public <T> T selectOne(String statement,Object[] parameter,Class pojo){
        String sql = configuration.getMappedStatement(statement);
        return executor.query(sql,parameter,pojo);
    }
}
