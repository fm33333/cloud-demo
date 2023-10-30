package com.demo.feign.data.official;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextMessage {

    private String toUserName;

    private String fromUserName;

    private long createTime;

    /**
     * 消息类型，text等
     */
    private String msgType;

    /**
     * 消息内容
     */
    private String content;

}
