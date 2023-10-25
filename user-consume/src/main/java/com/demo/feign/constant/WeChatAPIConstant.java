package com.demo.feign.constant;

public final class WeChatAPIConstant {
    private WeChatAPIConstant() {}

    public static final String CORP_ID = "corpid";
    public static final String CORP_SECRET = "corpsecret";
    public static final String GET_ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={corpid}&corpsecret={corpsecret}";


}
