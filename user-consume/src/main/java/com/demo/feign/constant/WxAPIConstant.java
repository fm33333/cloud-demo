package com.demo.feign.constant;

public final class WxAPIConstant {
    private WxAPIConstant() {
    }

    public static final String CORP_ID = "corpid";
    public static final String CORP_SECRET = "corpsecret";
    public static final String APP_ID = "appid";
    public static final String SECRET = "secret";
    public static final String ACCESS_TOKEN = "access_token";


    /* ========================== 企业微信 ===========================*/
    // 获取access_token的url
    public static final String CORP_GET_ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={corpid}&corpsecret={corpsecret}";

    // 应用发送消息的url
    public static final String CORP_SEND_MESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token={access_token}";


    /* ========================== 公众号 ===========================*/
    // 获取access_token的url
    public static final String OFFICIAL_GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={secret}";

    // 发送模板消息的url
    public static final String OFFICIAL_SEND_TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={access_token}";
}
