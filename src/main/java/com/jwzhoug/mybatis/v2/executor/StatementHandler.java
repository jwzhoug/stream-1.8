package com.jwzhoug.mybatis.v2.executor;

import com.jwzhoug.mybatis.v2.parameter.ParameterHandler;
import com.jwzhoug.mybatis.v2.session.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 封装JDBC Statement，用于操作数据库
 */
public class StatementHandler {

    private ResultSetHandler resultSetHandler = new ResultSetHandler();

    public <T> T query(String statement, Object[] parameter, Class pojo) {

        Connection conn = null;
        PreparedStatement psmt = null;
        Object result = null;

        try {

            conn = getConnection();
            psmt = conn.prepareStatement(statement);
            // 参数处理
            ParameterHandler parameterHandler = new ParameterHandler(psmt);
            parameterHandler.setParameters(parameter);
            psmt.execute();

            try {
                result = resultSetHandler.handle(psmt.getResultSet(),pojo);
            }catch (Exception e){
                e.printStackTrace();
            }
            return (T) result;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 获取连接
     *
     * @return
     */
    private Connection getConnection() {

        String driver = Configuration.properties.getString("jdbc.driver");
        String url = Configuration.properties.getString("jdbc.url");
        String username = Configuration.properties.getString("jdbc.username");
        String password = Configuration.properties.getString("jdbc.password");

        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
