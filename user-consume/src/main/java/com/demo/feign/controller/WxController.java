package com.demo.feign.controller;

import com.demo.feign.data.official.TextMessage;
import com.demo.feign.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 接入微信相关平台，包括企业微信、公众号
 */
@Slf4j
@RestController
@RequestMapping("wx")
public class WxController {

    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WechatService wechatService;


    /**
     * 公众号服务器配置的校验(校验过一次就可以修改了)
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @return 校验成功则返回echostr
     */
    @GetMapping("/verify")
    public String verify(@RequestParam("signature") String signature,
                         @RequestParam("timestamp") String timestamp,
                         @RequestParam("nonce") String nonce,
                         @RequestParam("echostr") String echostr) {
        log.info("wx|verify|signature:{}, timestamp:{}, nonce:{}, echostr:{}", signature, timestamp, nonce, echostr);
        // 校验
        boolean result = wxMpService.checkSignature(timestamp, nonce, signature);
        return result ? echostr : "fail";
    }

    /**
     * 公众号自动回复功能（url需和公众号配置的路径一致）
     *
     * @param request
     * @param response
     */
    @PostMapping("/verify")
    public void verify(HttpServletRequest request, HttpServletResponse response) {
        log.info("==========接收微信推送事件==========");
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
        } catch (IOException e) {
            log.error("Error setting character: {}", e.getMessage(), e);
        }
        wechatService.handleEvent(request, response);
    }

    /**
     * 测试模板消息
     *
     * @return
     */
    @PostMapping("/sendTemplateMessage")
    public String sendTemplateMessage() {
        // 推送模板消息
        try {
            wechatService.sendTemplateMessage();
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
        return "success";
    }

    /**
     * 开启内网穿透后，浏览器输入ppx.nat100.top/wx/test可访问此接口
     *
     * @return
     */
    @GetMapping("/test")
    public TextMessage test() {
        TextMessage textMessage = new TextMessage()
                .builder()
                .content("Hello, world!")
                .build();
        return textMessage;
    }

}
