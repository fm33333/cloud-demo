package cn.itcast.demo.producer;

import cn.itcast.demo.constant.KafkaConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static cn.itcast.demo.constant.KafkaConstant.*;

@Slf4j
@Component
public class BaseKafkaProducer {

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

    public void produce(String msg) {
        log.info("msg: {}", msg);
        String key = Uuid.randomUuid().toString();
        kafkaTemplate.send(KafkaConstant.DEFAULT_TOPIC, key, msg);
    }

    public void produceWithCallBack(String msg) {
        log.info("msg: {}", msg);
        String key = Uuid.randomUuid().toString();
        kafkaTemplate.send(KafkaConstant.DEFAULT_TOPIC, key, msg).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("发送消息失败：" + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                System.out.println("发送消息成功：" + result.getRecordMetadata().topic() + "-"
                        + result.getRecordMetadata().partition() + "-" + result.getRecordMetadata().offset());
            }
        });
    }
}
