package com.jwzhoug.mybatis.v2.executor;

import com.jwzhoug.mybatis.v2.cache.CacheKey;

import java.util.HashMap;
import java.util.Map;

/**
 * 带缓存的执行器，用于装饰基本执行器
 */
public class CachingExecutor implements Executor {

    private Executor delegate;
    private static final Map<Integer, Object> cache = new HashMap<>();

    public CachingExecutor(SimpleExecutor delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> T query(String statement, Object[] parameter, Class pojo) {

        CacheKey cacheKey = new CacheKey();
        cacheKey.update(statement);
        cacheKey.update(joinStr(parameter));

        if (cache.containsKey(cacheKey.getCode())) {
            // 命中缓存
            return (T) cache.get(cacheKey.getCode());
        } else {
            // 没有命中的话，调用被装饰的SimpleExecutor从数据库查询
            Object obj = delegate.query(statement, parameter, pojo);
            cache.put(cacheKey.getCode(), obj);
            return (T) obj;
        }

    }

    /**
     * 将参数数组 数据 进行串行整理
     *
     * @param objects
     * @return
     */
    private String joinStr(Object[] objects) {
        StringBuffer sb = new StringBuffer();
        if (objects != null && objects.length > 0) {
            for (Object object : objects) {
                sb.append(object.toString() + ",");
            }
        }
        int len = sb.length();
        if (len > 0) {
            sb.deleteCharAt(len - 1);
        }
        return sb.toString();
    }
}
