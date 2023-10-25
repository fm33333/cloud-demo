package com.demo.feign.controller;

import com.demo.feign.data.MailDTO;
import com.demo.feign.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/sendMail")
    public String sendMail(@RequestBody MailDTO mailDTO) {
        mailService.sendMail(mailDTO);
        return "success";
    }




}
