package cn.itcast.demo.consumer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.util.*;

/**
 * Kafka消费者（非@KafkaListener）
 * 不设置groupId，则默认使用配置文件中配的group-id
 * tips：
 * 1、同一个topic下，若消费者属于同一group，则会负载消费；否则每个消费者都会消费消息
 * 2、同一个topic下的一个partition只能被group中的一个消费者消费，若group中消费者数量超过了topic中partition的数量，那么多余的消费者会被闲置
 */
@Slf4j
@Getter
public class KafkaConsumerHandler {

    /**
     * KafkaConsumer
     */

    // 集群节点
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    // key和value的反序列化
    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;
    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;

    private KafkaConsumer<String, String> kafkaConsumer;
    // 主题
    private String topic;

    /**
     * 构建Kafka消费者
     *
     * @param topic
     * @param groupId
     * @return
     */
    public static KafkaConsumerHandler build(String topic, String groupId) {
        return new KafkaConsumerHandler(topic, groupId);
    }

    public KafkaConsumerHandler(String topic, String groupId) {
        log.info("Building Kafka Consumer...");
        this.setConsumer(groupId);
        log.info("after setConsumer: {}", this.kafkaConsumer);
        this.setTopic(topic);
    }

    /**
     * 消费者必要配置
     *
     * @return
     */
    public Properties consumerConfigs() {
        Properties props = new Properties();
        /*props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);*/
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.253.129:19092,192.168.253.129:29092,192.168.253.129:39092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    /**
     * 设置kafkaConsumer
     *
     * @param groupId
     * @return
     */
    public KafkaConsumerHandler setConsumer(String groupId) {
        Properties properties = this.consumerConfigs();
        // 设置groupId
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        log.info("properties: {}", properties.toString());
        this.kafkaConsumer = new KafkaConsumer<>(properties);
        return this;
    }

    /**
     * 设置主题
     *
     * @param topic
     * @return
     */
    public KafkaConsumerHandler setTopic(String topic) {
        kafkaConsumer.unsubscribe();
        this.kafkaConsumer.subscribe(Arrays.asList(topic));
        this.topic = topic;
        return this;
    }


    /**
     * 消费消息（需要无限循环调用）
     *
     * @param timeout 消费动作时间间隔（ms）
     * @return
     */
    public List<String> consume(Integer timeout) {
        List<String> messages = new ArrayList<>();
        try {
            // 注意：两次poll之间的时长不得超过session.timeout.ms=45s(?)（处理消费数据的总耗时不得超过max.poll.interval.ms=5min），否则会触发kafka的负载均衡，导致一定时间内无法从kafka服务端拉取数据
//            ConsumerRecords<String, String> records = this.kafkaConsumer.poll(Optional.ofNullable(timeout).orElse(100));
            ConsumerRecords<String, String> records = this.kafkaConsumer.poll(Duration.ofMillis(Optional.ofNullable(timeout).orElse(100)));
            for (ConsumerRecord<String, String> record : records) {
                log.info(">>>>record: {}", record);
                // topic是否一致
                if (!this.topic.equals(record.topic())) {
                    log.info("diffrent topic! poll topic is: {}, target topic is: {}", record.topic(), this.topic);
                } else {
                    messages.add(record.value());
                }
            }
        } catch (Exception e) {
            log.error("consume|exception: {}", e.getMessage(), e);
        }
        return messages;
    }

    /**
     * 关闭消费者
     */
    public void close() {
        this.kafkaConsumer.close();
    }

    /**
     * 唤醒消费者
     */
    public KafkaConsumerHandler wakeup() {
        this.kafkaConsumer.wakeup();
        return this;
    }


}
