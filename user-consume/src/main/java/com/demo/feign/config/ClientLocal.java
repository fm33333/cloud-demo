package com.demo.feign.config;

import com.demo.feign.clients.UserClient;

/**
 * 保存客戶端的ThreadLocal
 */
public class ClientLocal {
    private static final ThreadLocal<UserClient> CLIENT_THREAD_LOCAL = new ThreadLocal<>();

    public static UserClient get() {
        return CLIENT_THREAD_LOCAL.get();
    }

    public static void set(UserClient userClient) {
        CLIENT_THREAD_LOCAL.set(userClient);
    }

    public static void remove() {
        CLIENT_THREAD_LOCAL.remove();
    }

}
