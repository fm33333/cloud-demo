package com.demo.feign.data.official;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextMessage {

    private String toUserName;

    private String fromUserName;

    private long createTime;

    private String msgType;

    private String content;

}
