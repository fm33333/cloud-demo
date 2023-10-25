package com.demo.feign.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * caffeine缓存配置类
 */
public class CaffeineCacheConfig {

    public static final Map<String, Cache> CACHE_MAP = new HashMap<>();

    static {
        for (CacheNameEnum value : CacheNameEnum.values()) {
            Cache<Object, Object> cache = Caffeine.newBuilder()
                    .initialCapacity(value.getInitialCapacity())
                    .maximumSize(value.getMaximumSize())
                    .expireAfterAccess(Duration.ofSeconds(value.getExpire()))
                    .build();
            CACHE_MAP.put(value.name(), cache);
        }
    }

}
