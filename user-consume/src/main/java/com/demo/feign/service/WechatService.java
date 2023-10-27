package com.demo.feign.service;

import me.chanjar.weixin.common.error.WxErrorException;
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

    /**
     * 处理自动回复事件
     *
     * @param request
     * @param response
     */
    void handleEvent(HttpServletRequest request, HttpServletResponse response);

    /**
     * 发送模板消息
     */
    void sendTemplateMessage() throws WxErrorException;
}
