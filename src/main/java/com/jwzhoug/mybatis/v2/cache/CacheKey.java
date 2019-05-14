package com.jwzhoug.mybatis.v2.cache;

/**
 * 缓存Key
 */
public class CacheKey {

    // 默认哈希值
    private static final int DEFAULT_HASHCODE = 17;
    // 默认系数（乘）
    private static final int DEFAULT_MULTIPLIER = 37;

    private int hashcode;
    private int count;
    private int multiplier;

    public CacheKey() {
        this.hashcode = DEFAULT_HASHCODE;
        count = 0;
        this.multiplier = DEFAULT_MULTIPLIER;
    }

    /**
     * 返回CacheKey中的值
     *
     * @return
     */
    public int getCode() {
        return hashcode;
    }

    /**
     * 计算CacheKey中的HashCode
     *
     * @param object
     */
    public void update(Object object) {
        int baseHashcode = object == null ? 1 : object.hashCode();
        count++;
        baseHashcode *= count;
        hashcode = multiplier * hashcode + baseHashcode;
    }

}
