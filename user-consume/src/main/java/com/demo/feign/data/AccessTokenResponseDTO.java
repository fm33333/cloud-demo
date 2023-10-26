package com.demo.feign.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * weixin 获取access_token的dto
 */
@Data
@NoArgsConstructor
public class AccessTokenResponseDTO implements Serializable {

    // 出错返回码，0表示成功，非0表示调用失败
    private Integer errcode;

    // 返回码提示语
    private String errmsg;

    // 获取到的凭证
    private String access_token;

    // 凭证的有效时间（秒）
    private Integer expires_in;

}
