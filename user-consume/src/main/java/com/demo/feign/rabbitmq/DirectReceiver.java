package com.demo.feign.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 监听testDirectQueue队列，等待消费
 */
@Component
public class DirectReceiver {

    private static final String QUEUE_NAME = "test.direct";

    @RabbitHandler
    @RabbitListener(queues = QUEUE_NAME)
    public void process(String message) {
        System.out.println("第一个 DirectReceiver消费者收到消息：" + message);
    }

}
