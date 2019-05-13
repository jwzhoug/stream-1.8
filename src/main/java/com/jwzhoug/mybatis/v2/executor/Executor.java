package com.jwzhoug.mybatis.v2.executor;

/**
 * sql 执行器
 */
public interface Executor {
    <T> T query(String statement, Object[] parameter, Class pojo);
}
