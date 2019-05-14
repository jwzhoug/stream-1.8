package com.jwzhoug.mybatis.v2.binding;

import com.jwzhoug.mybatis.v2.session.DefaultSqlSession;

import javax.jws.Oneway;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * MapperProxy代理类，用于代理Mapper接口
 */
public class MapperProxy implements InvocationHandler {

    private DefaultSqlSession sqlSession;
    private Class object;

    public MapperProxy(DefaultSqlSession sqlSession, Class object) {
        this.sqlSession = sqlSession;
        this.object = object;
    }

    /**
     * Mapper接口的方法调用都会走到这里
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String mapperInterface = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String statementId = mapperInterface + "." + methodName;

        // 根据接口类型+方法名 能找到映射SQL，则执行SQL
        if (sqlSession.getConfiguration().hasStatement(statementId)){
            return sqlSession.selectOne(statementId,args,object);
        }

        // 否则执行被代理对象的原方法
        return method.invoke(proxy,args);
    }
}
