package com.demo.feign.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextMsgResponseDTO implements Serializable {

    private Integer errcode;

    private String errmsg;

    private String invaliduser;

    private String invalidparty;

    private String invalidtag;

    private String unlicenseuser;

    private String msgid;

    private String response_code;

}
