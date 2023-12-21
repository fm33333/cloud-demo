package cn.itcast.demo.consumer;

import cn.itcast.demo.constant.KafkaConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;

import java.util.Optional;

/**
 * Kafka消费者（@KafkaListener）
 */
@Slf4j
@Configuration
public class KafkaListenerHandler {

    @KafkaListener(topics = KafkaConstant.DEFAULT_TOPIC, groupId = KafkaConstant.GROUP_ID_0)
    public void consume1_1(ConsumerRecord<String, Object> record) {
        Optional<Object> value = Optional.ofNullable(record.value());
        log.info("consume1.1收到topic.1的消息：{} - {} - {}", record.key(), record.partition(), value);
    }

    @KafkaListener(topics = KafkaConstant.DEFAULT_TOPIC, groupId = KafkaConstant.GROUP_ID_1)
    public void consume1_2(ConsumerRecord<String, Object> record) {
        Optional<Object> value = Optional.ofNullable(record.value());
        log.info("consume1.2收到topic.1的消息：{} - {} - {}", record.key(), record.partition(), value);
    }

    /*@KafkaListener(groupId = KafkaConstant.GROUP_ID_2, topicPartitions = {
            @TopicPartition(topic = KafkaConstant.COLOR_TOPIC,
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "10"))
    })*/
    @KafkaListener(groupId = KafkaConstant.GROUP_ID_2, topicPartitions = {
            @TopicPartition(topic = KafkaConstant.COLOR_TOPIC,
                    partitions = "0")
    })
    public void consume2(ConsumerRecord<String, Object> record) {
        Optional<Object> value = Optional.ofNullable(record.value());
        log.info("consume2收到color的消息：{} - {} - {}", record.key(), record.partition(), value);
    }

}
