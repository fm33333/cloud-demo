package cn.itcast.demo.producer;

import cn.itcast.demo.constant.KafkaConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

/**
 * Kafka消费者（KafkaTemplate）
 */
@Slf4j
@Component
public class KafkaTemplateHandler {

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    public void produce(String topic, String key, String msg) {
        log.info("topic: {}, msg: {}", topic, msg);
        kafkaTemplate.send(topic, key, msg);
    }

    public void produce(String topic, String msg) {
        log.info("topic: {}, msg: {}", topic, msg);
        kafkaTemplate.send(topic, msg);
    }

    /**
     * 同步推送消息
     *
     * @param msg
     */
    public void produce(String msg) {
        log.info("msg: {}", msg);
        String key = Uuid.randomUuid().toString();
        try {
            SendResult<String, Object> sendResult = kafkaTemplate.send(KafkaConstant.DEFAULT_TOPIC, key, msg).get();
            log.info("sendResult: {}", sendResult);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 异步推送消息，带有回调
     *
     * @param msg
     */
    public void produceWithCallBack(String msg) {
        log.info("msg: {}", msg);
        String key = Uuid.randomUuid().toString();
        kafkaTemplate.send(KafkaConstant.DEFAULT_TOPIC, key, msg).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("发送消息失败：{}", ex.getCause(), ex);
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("发送消息成功：{}-{}-{}", result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });
    }
}
