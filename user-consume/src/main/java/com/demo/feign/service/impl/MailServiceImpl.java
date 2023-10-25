package com.demo.feign.service.impl;

import com.demo.feign.data.MailDTO;
import com.demo.feign.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(MailDTO mailDTO) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(mailDTO.getFrom());
        simpleMailMessage.setTo(mailDTO.getTo());
        simpleMailMessage.setSubject(mailDTO.getSubject());
        simpleMailMessage.setText(mailDTO.getText());
        javaMailSender.send(simpleMailMessage);
    }
}
