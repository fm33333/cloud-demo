package com.demo.feign.config;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpMapConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxConfig {

    @Value("${official.appid}")
    private String appid;
    @Value("${official.appsecret}")
    private String appsecret;
    @Value("${official.token}")
    private String token;
    @Value("${official.aeskey}")
    private String aeskey;

    @Bean
    public WxMpService wxMpService() {
        WxMpMapConfigImpl wxMpMapConfig = new WxMpMapConfigImpl();
        wxMpMapConfig.setAppId(appid);
        wxMpMapConfig.setSecret(appsecret);
        wxMpMapConfig.setToken(token);
        wxMpMapConfig.setAesKey(aeskey);
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpMapConfig); // 集群部署最好不要使用
        // 设置多个微信公众号的配置
        // wxMpService.setMultiConfigStorages();
        return wxMpService;
    }
}
