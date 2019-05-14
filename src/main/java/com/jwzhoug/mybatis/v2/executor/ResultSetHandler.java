package com.jwzhoug.mybatis.v2.executor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集处理器
 */
public class ResultSetHandler {


    public <T> T handle(ResultSet resultSet, Class type) {
        // 直接调用Class方法产生一个实例
        Object pojo = null;
        try {
            pojo = type.newInstance();
            // 遍历结果集
            if (resultSet.next()) {
                for (Field field : pojo.getClass().getDeclaredFields()) {
                    setValue(pojo, field, resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (T) pojo;
    }

    /**
     * 通过反射给属性赋值
     *
     * @param pojo
     * @param field
     * @param resultSet
     */
    private void setValue(Object pojo, Field field, ResultSet resultSet) {
        try {

            Method setMethod = pojo.getClass()
                    .getMethod("set" + firstWordCapital(field.getName()), field.getType());
            setMethod.invoke(pojo, getResult(resultSet, field));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据反射判断类型，从ResultSet中取对应类型参数
     *
     * @param resultSet
     * @param field
     * @return
     */
    private Object getResult(ResultSet resultSet, Field field) throws SQLException {
        Class type = field.getType();
        String dataName = humpToUnderline(field.getName());
        if (Integer.class == type) {
            return resultSet.getInt(dataName);
        } else if (String.class == type) {
            return resultSet.getString(dataName);
        } else if (Long.class == type) {
            return resultSet.getLong(dataName);
        } else if (Boolean.class == type) {
            return resultSet.getBoolean(dataName);
        } else if (Double.class == type) {
            return resultSet.getDouble(dataName);
        } else {
            return resultSet.getString(dataName);
        }
    }

    /**
     * 驼峰 转 下划线
     *
     * @param para
     * @return
     */
    private String humpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;
        if (!para.contains("_")) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, "_");
                    temp += 1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 首字母大写
     *
     * @param word
     * @return
     */
    private String firstWordCapital(String word) {
        byte[] bytes = word.getBytes();
        bytes[0] = (byte) (bytes[0] - 32);
        return new String(bytes);
    }

}
