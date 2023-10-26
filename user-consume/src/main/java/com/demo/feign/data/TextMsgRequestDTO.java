package com.demo.feign.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextMsgRequestDTO {

    // touser、toparty、totag不能同时为空
    private String touser;

    private String toparty;

    private String totag;

    /**
     * 必需
     * 消息类型 text
     */
    private String msgtype;

    /**
     * 必需
     * 企业应用的id 1000002
     */
    private Integer agentid;

    /**
     * 必需
     * 消息内容
     */
    private Text text;

    // 是否是保密消息
    private Integer safe;

    private Integer enable_id_trans;

    private Integer enable_duplicate_check;

    private Integer duplicate_check_interval;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Text {
        private String content;
    }
}
