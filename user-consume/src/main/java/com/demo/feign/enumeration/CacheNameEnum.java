package com.demo.feign.enumeration;

/**
 * 缓存配置枚举类
 */
public enum CacheNameEnum {

    /**
     * 客戶端
     */
    USER_CLIENT(10, 20L, 60*60L);

    /**
     * 初始化容量
     */
    private final Integer initialCapacity;

    /**
     * 最大容量
     */
    private final Long maximumSize;

    /**
     * 过期时间 秒
     */
    private final Long expire;

    CacheNameEnum(Integer initialCapacity, Long maximumSize, Long expire) {
        this.initialCapacity = initialCapacity;
        this.maximumSize = maximumSize;
        this.expire = expire;
    }

    public Long getMaximumSize() {
        return maximumSize;
    }

    public Integer getInitialCapacity() {
        return initialCapacity;
    }

    public Long getExpire() {
        return expire;
    }
}