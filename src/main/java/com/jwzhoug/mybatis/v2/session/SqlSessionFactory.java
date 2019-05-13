package com.jwzhoug.mybatis.v2.session;

public class SqlSessionFactory {

    private Configuration configuration;

    /**
     * build方法用于初始化Configuration,
     * 解析配置文件的工作
     *
     * @return
     */
    public SqlSessionFactory build() {
        configuration = new Configuration();
        return this;
    }

    public DefaultSqlSession openSqlSession(){
        return new DefaultSqlSession(configuration);
    }

}
