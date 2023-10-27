package com.demo.feign.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信 服务
 */
@Service
public interface WechatService {

    /**
     * 获取 access_token
     *
     * @return
     */
    String getAccessToken();

    void handleEvent(HttpServletRequest request, HttpServletResponse response);
}
