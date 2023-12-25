package cn.itcast.demo.consumer;

import cn.itcast.demo.constant.KafkaConstant;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.time.Duration;
import java.util.*;


/**
 * Kafka消费者 构造类（非@KafkaListener，运行时构建，方便其他服务来调用）
 * 若未指定groupId，则默认使用配置文件中配的group-id
 * tips：
 * 1、同一个topic下，若消费者属于同一group，则会负载消费；否则每个消费者都会消费消息
 * 2、同一个topic下的一个partition只能被group中的一个消费者消费，若group中消费者数量超过了topic中partition的数量，那么多余的消费者会被闲置
 */
@Slf4j
@Getter
public class KafkaConsumerBuilder {

    //Kafka消费者
    private KafkaConsumer<String, String> kafkaConsumer;
    // 主题
    private String topic;
    // 分组id
    private String groupId;

    /**
     * 构建Kafka消费者
     *
     * @param topic
     * @param groupId
     * @return
     */
    public static KafkaConsumerBuilder build(String topic, String groupId) {
        return new KafkaConsumerBuilder(topic, groupId);
    }

    public KafkaConsumerBuilder(String topic, String groupId) {
        log.info("Building Kafka Consumer...topic={}, groupId={}", topic, groupId);
        this.setConsumer(groupId);
        log.info("after setConsumer: {}", this.kafkaConsumer);
        this.setTopic(topic);
        log.info("after setTopic: {}", this.topic);
    }

    /**
     * 消费者必要配置
     *
     * @return
     */
    public Properties consumerConfigs() {
        Properties props = new Properties();
        // TODO: 此处固定了bootstrapServers，实际应该传参
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaConstant.STRING_DESERIALIZER);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaConstant.STRING_DESERIALIZER);
        // 是否自动提交offset
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        // 提交offset的时间间隔
        // props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return props;
    }

    /**
     * 设置kafkaConsumer
     *
     * @param groupId
     * @return
     */
    public KafkaConsumerBuilder setConsumer(String groupId) {
        Properties props = this.consumerConfigs();
        // 设置groupId
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        this.groupId = groupId;
        this.kafkaConsumer = new KafkaConsumer<>(props);
        return this;
    }

    /**
     * 设置主题
     *
     * @param topic
     * @return
     */
    public KafkaConsumerBuilder setTopic(String topic) {
        this.topic = topic;
        kafkaConsumer.unsubscribe();
        this.kafkaConsumer.subscribe(Arrays.asList(topic));
        return this;
    }

    /**
     * 消费消息
     */
    public void consume() {
        try {
            while (true) {
                // 拉取消息，间隔xxx ms
                List<String> messages = this.consume(100);
                if (!messages.isEmpty()) {
                    log.info("KafkaConsumer [{}] 消费成功>>>> topic: {}, messages: {}", this.groupId, this.getTopic(), messages);
                }
                // 手动提交offset
                this.commitSync();
            }
        } catch (Exception e) {
            log.error("消费异常|exception: {}", e.getMessage(), e);
        } finally {
            this.close();
        }
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
     * 手动同步提交offset
     */
    public void commitSync() {
        this.kafkaConsumer.commitSync();
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
    public KafkaConsumerBuilder wakeup() {
        this.kafkaConsumer.wakeup();
        return this;
    }


}
