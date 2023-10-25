package com.demo.feign.utils;

import com.demo.feign.clients.UserClient;
import com.demo.feign.config.CaffeineCacheConfig;
import com.demo.feign.config.DynamicFeignClientFactory;
import com.demo.feign.enumeration.CacheNameEnum;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;

/**
 * 缓存工具类
 */
@Slf4j
public class CacheUtil {
    // 用户客户端缓存
    private static final Cache USER_CLIENT_CACHE = CaffeineCacheConfig.CACHE_MAP.get(CacheNameEnum.USER_CLIENT.name());

    /**
     * 清除缓存
     * @param key 缓存key
     */
    public static void cleanCache(String key) {
        log.info("cleanCache: {}", USER_CLIENT_CACHE.asMap());
        USER_CLIENT_CACHE.invalidate(key);
        log.info("cleanCache: {}", USER_CLIENT_CACHE.asMap());
    }

    /**
     * 获取用户客户端
     * @param feignClientFactory client工厂
     * @param serverName 服务名
     * @param uri 接口路径
     * @return
     */
    public static UserClient getUserClient(DynamicFeignClientFactory<UserClient> feignClientFactory
            , String serverName, String uri) {
        UserClient userClient;
        // 判断是否已缓存
        if (USER_CLIENT_CACHE.getIfPresent(serverName  + uri) == null) {
            userClient = feignClientFactory.getFeignClient(
                    UserClient.class, serverName, uri.toString());
            log.info("CacheUtil|getUserClient|create userClient by factory: {}", userClient);
            USER_CLIENT_CACHE.put(serverName + uri, userClient);
        } else {
            userClient = (UserClient) USER_CLIENT_CACHE.getIfPresent(serverName + uri);
            log.info("CacheUtil|getUserClient|get userClient from cache: {}", userClient);
        }
        return userClient;
    }

}
