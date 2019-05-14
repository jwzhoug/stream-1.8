package com.jwzhoug.mybatis.v2.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParameterHandler {

    private PreparedStatement psmt;

    public ParameterHandler(PreparedStatement psmt) {
        this.psmt = psmt;
    }

    /**
     * 从方法中获取参数，遍历设置SQL中的？占位符
     *
     * @param parameter
     */
    public void setParameters(Object[] parameter) {
        try {
            // PreparedStatement的序号是从1开始
            for (int i = 0; i < parameter.length; i++) {
                int k = i + 1;
                if (parameter[i] instanceof Integer) {
                    psmt.setInt(k, (Integer) parameter[i]);
                } else if (parameter[i] instanceof Long) {
                    psmt.setLong(k, (Long) parameter[i]);
                } else if (parameter[i] instanceof String) {
                    psmt.setString(k, String.valueOf(parameter[i]));
                } else if (parameter[i] instanceof Boolean) {
                    psmt.setBoolean(k, (Boolean) parameter[i]);
                } else {
                    psmt.setString(k, String.valueOf(parameter[i]));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
