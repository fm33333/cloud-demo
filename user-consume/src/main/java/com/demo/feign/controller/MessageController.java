package com.demo.feign.controller;

import com.demo.feign.cache.CacheNameEnum;
import com.demo.feign.cache.CaffeineCacheConfig;
import com.demo.feign.constant.WeChatAPIConstant;
import com.demo.feign.data.AccessTokenResponseDTO;
import com.demo.feign.data.TextMsgRequestDTO;
import com.demo.feign.data.TextMsgResponseDTO;
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
public class MessageController {

    private static final Cache ACCESS_TOKEN_CACHE = CaffeineCacheConfig.CACHE_MAP.get(CacheNameEnum.ACCESS_TOKEN.name());
    @Value("${corp.corpid}")
    private String corpid;
    @Value("${corp.corpsecret}")
    private String corpsecret;
    @Value("${corp.agentid}")
    private String agentid;

    @PostMapping("/send")
    public String send() {
        // 1. 获取access_token
        AccessTokenResponseDTO accessTokenResponseDTO = new AccessTokenResponseDTO();
        if (null == ACCESS_TOKEN_CACHE.getIfPresent(agentid)) {
            String url = WeChatAPIConstant.GET_ACCESS_TOKEN_URL;
            Map<String, Object> params = new HashMap<>();
            params.put(WeChatAPIConstant.CORP_ID, corpid);
            params.put(WeChatAPIConstant.CORP_SECRET, corpsecret);
            accessTokenResponseDTO = HttpUtil.get(url, AccessTokenResponseDTO.class, params).getBody();
            // 放入缓存
            ACCESS_TOKEN_CACHE.put(agentid, accessTokenResponseDTO);
            log.info("MessageController|send|get new one, accessTokenResponseDTO: {}", accessTokenResponseDTO.toString());
        } else {
            // 从缓存中拿凭证
            accessTokenResponseDTO = (AccessTokenResponseDTO) ACCESS_TOKEN_CACHE.getIfPresent(agentid);
            log.info("MessageController|send|get from cache, accessTokenResponseDTO: {}", accessTokenResponseDTO.toString());
        }

        // 2. 构造消息体
        String url = WeChatAPIConstant.SEND_MESSAGE_URL;
        Map<String, Object> params = new HashMap<>();
        params.put(WeChatAPIConstant.ACCESS_TOKEN, accessTokenResponseDTO.getAccess_token());
        TextMsgRequestDTO textMsgRequestDTO = new TextMsgRequestDTO().builder()
                .touser("@all")
                .msgtype("text")
                .agentid(Integer.parseInt(agentid))
                .text(new TextMsgRequestDTO.Text("你好你好你好！"))
                .safe(0)
                .build();
        log.info("MessageController|send|textMsgRequestDTO: {}", textMsgRequestDTO.toString());

        // 3. 发送消息
        ResponseEntity<TextMsgResponseDTO> msgResponseDTOResponseEntity = HttpUtil.post(
                url, textMsgRequestDTO, TextMsgResponseDTO.class, params);
        log.info("MessageController|send|msgResponseDTOResponseEntity: {}", msgResponseDTOResponseEntity.getBody());


        return "success";
    }
}
