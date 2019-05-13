package com.jwzhoug.mybatis.v2.mapper;

import com.jwzhoug.mybatis.v2.session.DefaultSqlSession;

public class MapperRegistry {


    public void addMapper(Class mapper, Class pojo) {

    }

    public <T> T getMapper(Class<T> clazz, DefaultSqlSession sqlSession) {
        return null;
    }
}
