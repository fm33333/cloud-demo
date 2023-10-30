package com.demo.feign.service.impl;

import com.demo.feign.cache.CacheNameEnum;
import com.demo.feign.cache.CaffeineCacheConfig;
import com.demo.feign.constant.WxConstant;
import com.demo.feign.data.AccessTokenResponse;
import com.demo.feign.data.official.TextMessage;
import com.demo.feign.service.WechatService;
import com.demo.feign.utils.HttpUtil;
import com.demo.feign.utils.XmlUtil;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信 服务
 * TODO：修改发送请求，调用依赖包中的方法即可
 */
@Slf4j
@Service
public class WechatServiceImpl implements WechatService {
    private static final Cache ACCESS_TOKEN_CACHE = CaffeineCacheConfig.CACHE_MAP.get(CacheNameEnum.ACCESS_TOKEN.name());

    @Autowired
    private WxMpService wxMpService;
    @Value("${wx.official.template_id_01}")
    private String templateId_01;

    // wxMpService能自动刷新access_token，不需要自己再去获取了
    @Override
    public String getAccessToken() {
        String appId = wxMpService.getWxMpConfigStorage().getAppId();
        String secret = wxMpService.getWxMpConfigStorage().getSecret();
        // TODO access_token定时刷新
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
        if (null == ACCESS_TOKEN_CACHE.getIfPresent(appId)) {
            String url = WxConstant.OFFICIAL_GET_ACCESS_TOKEN_URL;
            Map<String, Object> params = new HashMap<>();
            params.put(WxConstant.APP_ID, appId);
            params.put(WxConstant.SECRET, secret);
            log.info("getAccessToken|params: {}", params);
            accessTokenResponse = HttpUtil.get(url, AccessTokenResponse.class, params).getBody();
            // 放入缓存 TODO 只放access_token
            ACCESS_TOKEN_CACHE.put(appId, accessTokenResponse);
            log.info("getAccessToken|get new one, accessTokenResponse: {}", accessTokenResponse);
        } else {
            // 从缓存中拿凭证
            accessTokenResponse = (AccessTokenResponse) ACCESS_TOKEN_CACHE.getIfPresent(appId);
            assert accessTokenResponse != null;
            log.info("getAccessToken|get from cache, accessTokenResponse: {}", accessTokenResponse);
        }
        return accessTokenResponse.getAccessToken();
    }

    @Override
    public void handleEvent(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 获取用户发送内容（XML）
            ServletInputStream in = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String tempStr = "";   //作为输出字符串的临时串，用于判断是否读取完毕
            String postData = "";
            while (null != (tempStr = reader.readLine())) {
                postData += tempStr;
            }
            log.info("handleEvent|postData: {}", postData);
            // 此时postData是XML字符串，需要解析
            Map<String, String> parseXML = XmlUtil.parseXML(postData);
            log.info("handleEvent|parseXML: {}", parseXML);

            // 用户发送的是普通消息
            Map<String, Object> map = new HashMap<>();
            map.put("FromUserName", parseXML.get("FromUserName"));
            map.put("ToUserName", parseXML.get("ToUserName"));
            // 封装回复消息
            String returnMsg = getReturnMsg(map);
            log.info("handleEvent|returnMsg: {}", returnMsg);
            // 发送回复消息
            response.getWriter().print(returnMsg);

        } catch (IOException e) {
            log.error("处理微信公众号请求异常：", e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public String getReturnMsg(Map<String, Object> decryptMap) {
        log.info("---开始封装xml---decryptMap: {}", decryptMap.toString());
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(decryptMap.get("FromUserName").toString());
        textMessage.setFromUserName(decryptMap.get("ToUserName").toString());
        textMessage.setCreateTime(System.currentTimeMillis());
        textMessage.setMsgType("text");
        textMessage.setContent("[自动回复]\n你好！欢迎你！！！\n\n这里是 【这不是fming】 公众号！");
        log.info("---封装完毕---textMessage: {}", textMessage);
        return XmlUtil.getXmlString(textMessage);
    }

    /**
     * 推送模板消息
     */
    @Override
    public void sendTemplateMessage() throws WxErrorException {

        // 构建消息体
        List<WxMpTemplateData> dataList = new ArrayList<>();
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("name", "fming");
        dataMap.put("date", LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            dataList.add(new WxMpTemplateData(entry.getKey(), entry.getValue()));
        }
//        dataList.add(new WxMpTemplateData("name", "fmh")); // name为模板中{{name.DATA}}的name
//        dataList.add(new WxMpTemplateData("date", ));

        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        wxMpTemplateMessage.setToUser("oppoO6w6c0O4CDYXgDXElhkRi_a8"); // 用户openid
        wxMpTemplateMessage.setTemplateId(templateId_01); // 模板id
        wxMpTemplateMessage.setData(dataList);

        // 发送模板消息
        String msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
//        log.info("============================access_token: {}, {}", wxMpService.getAccessToken(), msgId);
    }


}
