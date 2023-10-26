package com.demo.feign.constant;

public final class WeChatAPIConstant {
    private WeChatAPIConstant() {
    }

    public static final String CORP_ID = "corpid";
    public static final String CORP_SECRET = "corpsecret";
    public static final String ACCESS_TOKEN = "access_token";


    // 获取access_token的url
    public static final String GET_ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={corpid}&corpsecret={corpsecret}";

    // 应用发送消息的url
    public static final String SEND_MESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token={access_token}";

}
