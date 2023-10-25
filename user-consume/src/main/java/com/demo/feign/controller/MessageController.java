package com.demo.feign.controller;

import com.demo.feign.constant.WeChatAPIConstant;
import com.demo.feign.data.AccessTokenDTO;
import com.demo.feign.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    @Value("${corp.corpid}")
    private String corpid;
    @Value("${corp.corpsecret}")
    private String corpsecret;

    @PostMapping("/send")
    public String send() {
        // 1. 获取access_token
        // TODO: 缓存access_token
        String url = WeChatAPIConstant.GET_ACCESS_TOKEN_URL;
        Map<String, Object> params = new HashMap<>();
        params.put(WeChatAPIConstant.CORP_ID, corpid);
        params.put(WeChatAPIConstant.CORP_SECRET, corpsecret);
        AccessTokenDTO accessTokenDTO = HttpUtil.get(url, AccessTokenDTO.class, params).getBody();
        log.info("MessageController|send|accessTokenDTO: {}", accessTokenDTO.toString());

        // 2. 构造消息体


        // 3. 发送消息

        return "success";
    }
}
