package com.demo.feign.data;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MailDTO implements Serializable {

    // 发件人
    private String from;
    // 收件人
    private String to;
    // 邮件主题
    private String subject;
    // 邮件内容
    private String text;

    private static final long serialVersionUID = 1L;
}