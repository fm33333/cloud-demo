package cn.itcast.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Optional;

import static cn.itcast.demo.constant.KafkaConstant.*;

/**
 * Kafka消费者
 * TODO：参考common-kafka中的动态设置topics等属性
 * tips：
 *      1、同一个topic下，若消费者属于同一group，则会负载消费；否则每个消费者都会消费消息
 *      2、同一个topic下的一个partition只能被group中的一个消费者消费，若group中消费者数量超过了topic中partition的数量，那么多余的消费者会被闲置
 */
@Configuration
public class BaseKafkaConsumer {

    @KafkaListener(topics = DEFAULT_TOPIC, groupId = GROUP_ID_0)
    public void consume1_1(ConsumerRecord<String, Object> record) {
        Optional<Object> value = Optional.ofNullable(record.value());
        System.out.println("consume1.1收到topic.1的消息：" + record.key() + " - " + record.partition() + " - " + record.value());
    }

    @KafkaListener(topics = DEFAULT_TOPIC, groupId = GROUP_ID_1)
    public void consume1_2(ConsumerRecord<String, Object> record) {
        Optional<Object> value = Optional.ofNullable(record.value());
        System.out.println("consume1.2收到topic.1的消息：" + record.key() + " - " + record.partition() + " - " + record.value());
    }

    /**
     * 消费（不设置groupId，则默认使用配置文件中配的group-id）
     *
     * @param record
     */
    @KafkaListener(topics = COLOR_TOPIC)
    public void consume2(ConsumerRecord<String, Object> record) {
        Optional<Object> value = Optional.ofNullable(record.value());
        System.out.println("consume2收到color的消息：" + record.key() + " - " + record.partition() + " - " + record.value());
    }

}
