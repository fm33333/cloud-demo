package com.demo.feign.service;

import com.demo.feign.data.MailDTO;
import org.springframework.stereotype.Service;

@Service
public interface MailService {

    /**
     * 发邮件
     */
    public void sendMail(MailDTO mailDTO);

}
