package com.demo.feign.config;

import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * FeignClient工厂
 *
 * @param <T>
 */
@Component
public class DynamicFeignClientFactory<T> {

    private final FeignClientBuilder feignClientBuilder;

    public DynamicFeignClientFactory(ApplicationContext appContext) {
        this.feignClientBuilder = new FeignClientBuilder(appContext);
    }

    /**
     * 构建FeignClient实例
     * @param type feignClient.class
     * @param serverName 提供服务方的服务名
     * @param path 路径
     * @return
     */
    public T getFeignClient(final Class<T> type, String serverName, String path) {
        return this.feignClientBuilder
                .forType(type, serverName)
                .path(path)
                .build();
    }
}
