package com.demo.feign.config;

import com.demo.feign.enumeration.CacheNameEnum;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存配置类
 * 初始化缓存 Map
 */
public class CaffeineCacheConfig {
    public static final Map<String, Cache> CACHE_MAP = new HashMap<>();

    static {

        for (CacheNameEnum value : CacheNameEnum.values()) {

            Cache<Object, Object> cache = Caffeine.newBuilder()
                    .initialCapacity(value.getInitialCapacity())
                    .maximumSize(value.getMaximumSize())
                    //读写（自动刷新过期时间）后失效时间
                    .expireAfterAccess(Duration.ofSeconds(value.getExpire()))
                    .build();

            CACHE_MAP.put(value.name(), cache);
        }
    }
}