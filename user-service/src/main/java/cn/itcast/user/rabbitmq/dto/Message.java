package cn.itcast.user.rabbitmq.dto;

import lombok.Data;

@Data
public class Message {
    private String messageId;
    private String messageContent;
    private String createTime;
}
