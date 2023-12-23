package cn.itcast.demo.controller;

import cn.itcast.demo.consumer.KafkaConsumerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Kafka消费消息控制类
 */
@Slf4j
@RestController
public class KafkaConsumerController {


    /**
     * 消费消息
     * 有新消息时，会跳过拉取消息的timeout，直接走下一个循环拉取新的消息
     * 若同时有多条消息，可一次性消费多条（具体上限待查询，应该是配置中的某个默认值）
     *
     * @param topic   主题
     * @param groupId 分组id
     * @return
     */
    @GetMapping("/consume")
    public void consume(@RequestParam String topic, @RequestParam String groupId) {
        log.info("kafkaConsumer开始消费==== topic: {}, groupId: {}", topic, groupId);
        // 选择这样动态创建而不是在配置类中构建Bean的原因：此模块放入标准化组件时，其他服务调用时也可以使用这种传参方式创建
        KafkaConsumerBuilder consumerBuilder = KafkaConsumerBuilder.build(topic, groupId);
        try {
            while (true) {
                // 拉取消息，间隔xxx ms
                List<String> messages = consumerBuilder.consume(100);
                if (!messages.isEmpty()) {
                    log.info("KafkaConsumer [{}] 消费成功>>>> topic: {}, messages: {}", groupId, consumerBuilder.getTopic(), messages);
                }
                // 手动提交offset
                consumerBuilder.commitSync();
            }
        } catch (Exception e) {
            log.error("消费异常|exception: {}", e.getMessage(), e);
        } finally {
            consumerBuilder.close();
        }
    }

    @GetMapping("/wakeup")
    public void wakeup() {

    }
}
