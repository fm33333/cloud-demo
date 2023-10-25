package cn.itcast.user.rabbitmq.controller;

import cn.itcast.user.rabbitmq.constant.RabbitMQConstant;
import cn.itcast.user.rabbitmq.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 测试rabbitmq发消息
 */
@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/sendDirect")
    public String send(@RequestBody Message message) {
        message.setMessageId(String.valueOf(UUID.randomUUID()));
        message.setCreateTime(LocalDateTime.now().format(DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")));
        rabbitTemplate.convertAndSend(RabbitMQConstant.DIRECT_NAME, RabbitMQConstant.DIRECT_ROUTING_KEY
                , message.toString());
        return "success: " + message;
    }

}
