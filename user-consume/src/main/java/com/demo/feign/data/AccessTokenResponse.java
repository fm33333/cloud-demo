package com.demo.feign.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * weixin 获取access_token的dto
 */
@Data
@NoArgsConstructor
public class AccessTokenResponse implements Serializable {

    // 出错返回码，0表示成功，非0表示调用失败
    @JsonProperty(value = "errcode")
    private Integer errCode;

    // 返回码提示语
    @JsonProperty(value = "errmsg")
    private String errMsg;

    // 获取到的凭证
    @JsonProperty(value = "access_token")
    private String accessToken;

    // 凭证的有效时间（秒）
    @JsonProperty(value = "expires_in")
    private Integer expiresIn;

}
