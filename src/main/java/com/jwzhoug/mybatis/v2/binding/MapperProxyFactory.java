package com.jwzhoug.mybatis.v2.binding;

import com.jwzhoug.mybatis.v2.session.DefaultSqlSession;

import java.lang.reflect.Proxy;

/**
 * 用于产生MapperProxy代理类
 *
 * @param <T>
 */
public class MapperProxyFactory<T> {

    private Class<T> mapperInterface;
    private Class object;

    public MapperProxyFactory(Class<T> mapper, Class pojo) {
        this.mapperInterface = mapper;
        this.object = pojo;
    }

    public T newInstance(DefaultSqlSession sqlSession) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(),
                new Class[]{mapperInterface}, new MapperProxy(sqlSession, object));
    }
}
