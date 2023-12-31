package com.demo.feign.controller;

import com.demo.feign.cache.CacheNameEnum;
import com.demo.feign.cache.CaffeineCacheConfig;
import com.demo.feign.constant.WxConstant;
import com.demo.feign.data.AccessTokenResponse;
import com.demo.feign.data.corp.TextMsgRequestDTO;
import com.demo.feign.data.corp.TextMsgResponseDTO;
import com.demo.feign.utils.HttpUtil;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/message")
public class CorpWxController {

    private static final Cache ACCESS_TOKEN_CACHE = CaffeineCacheConfig.CACHE_MAP.get(CacheNameEnum.ACCESS_TOKEN.name());
    @Value("${wx.corp.corpid}")
    private String corpid;
    @Value("${wx.corp.corpsecret}")
    private String corpsecret;
    @Value("${wx.corp.agentid}")
    private String agentid;

    /**
     * 推送消息到企业微信
     * TODO：看看有没有测试号可以申请
     *
     * @return
     */
    @PostMapping("/sendToCorp")
    public String sendToCorp() {
        // 1. 获取access_token
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
        if (null == ACCESS_TOKEN_CACHE.getIfPresent(agentid)) {
            String url = WxConstant.CORP_GET_ACCESS_TOKEN_URL;
            Map<String, Object> params = new HashMap<>();
            params.put(WxConstant.CORP_ID, corpid);
            params.put(WxConstant.CORP_SECRET, corpsecret);
            accessTokenResponse = HttpUtil.get(url, AccessTokenResponse.class, params).getBody();
            // 放入缓存
            ACCESS_TOKEN_CACHE.put(agentid, accessTokenResponse);
            log.info("CorpWxController|send|get new one, accessTokenResponse: {}", accessTokenResponse.toString());
        } else {
            // 从缓存中拿凭证
            accessTokenResponse = (AccessTokenResponse) ACCESS_TOKEN_CACHE.getIfPresent(agentid);
            log.info("CorpWxController|send|get from cache, accessTokenResponse: {}", accessTokenResponse.toString());
        }

        // 2. 构造消息体
        Map<String, Object> params = new HashMap<>();
        params.put(WxConstant.ACCESS_TOKEN, accessTokenResponse.getAccessToken());
        TextMsgRequestDTO textMsgRequestDTO = new TextMsgRequestDTO().builder()
                .touser("@all")
                .msgtype("text")
                .agentid(Integer.parseInt(agentid))
                .text(new TextMsgRequestDTO.Text("你好你好你好！"))
                .safe(0)
                .build();
        log.info("CorpWxController|send|textMsgRequestDTO: {}", textMsgRequestDTO.toString());

        // 3. 发送消息
        String url = WxConstant.CORP_SEND_MESSAGE_URL;
        ResponseEntity<TextMsgResponseDTO> msgResponseEntity = HttpUtil.post(
                url, textMsgRequestDTO, TextMsgResponseDTO.class, params);
        log.info("CorpWxController|send|msgResponseEntity: {}", msgResponseEntity.getBody());

        return "success";
    }

//
//    @PostMapping("/sendToOfficial")
//    public String
}
