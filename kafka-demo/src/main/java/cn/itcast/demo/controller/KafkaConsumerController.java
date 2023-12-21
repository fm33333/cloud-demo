package cn.itcast.demo.controller;

import cn.itcast.demo.consumer.KafkaConsumerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Kafka消费消息控制类
 */
@Slf4j
@RestController
public class KafkaConsumerController {


    /**
     * 消费消息
     * 有消息时，会跳过阻塞时间，直接走下一个循环拉取新的消息，且每次只能拉取一条消息(可以配置批量拉取)
     *
     * @param topic
     * @param groupId
     * @return
     */
    @GetMapping("/consume")
    public String consume(@RequestParam String topic, @RequestParam String groupId) {
        log.info("topic: {}, groupId: {}", topic, groupId);
        KafkaConsumerHandler consumer = KafkaConsumerHandler.build(topic, groupId);
        try {
            int i = 5;
            // 正常应该while(true)
            while (true) {
                log.info("进入循环...当前时间：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                // 拉取消息，阻塞xxx ms
                List<String> messages = consumer.consume(5000);
                if (!messages.isEmpty()) {
                    log.info("示例消费成功>>>> topic: {}, messages: {}", consumer.getTopic(), messages);
                }
            }
        } catch (Exception e) {
            log.error("示例消费|exception: {}", e.getMessage(), e);
        } finally {
            consumer.close();
        }
        return "ok";
    }

    @GetMapping("/wakeup")
    public void wakeup() {

    }
}
