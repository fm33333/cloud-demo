package cn.itcast.demo.producer;

import cn.itcast.demo.constant.KafkaConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.Uuid;

import java.util.Properties;

/**
 * Kafka Producer构造类（运行时构建）
 */
@Slf4j
public class KafkaProducerBuilder {

    // Kafka生产者
    private KafkaProducer<String, String> kafkaProducer;

    public static KafkaProducerBuilder build(String bootStrapServers) {
        return new KafkaProducerBuilder(bootStrapServers);
    }

    public KafkaProducerBuilder(String bootStrapServers) {
        // 构造生产者
        this.setProducer(bootStrapServers);
    }

    /**
     * 生产者配置
     *
     * @return
     */
    public Properties producerConfigs() {
        Properties props = new Properties();
        // TODO: 此处固定了bootstrapServers，实际应该传参
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaConstant.STRING_SERIALIZER);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaConstant.STRING_SERIALIZER);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 30 * 1000);
        // 重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, 5);
        props.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, 3000);
        // 自定义分区器
        // props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "分区器包路径");
        // 幂等校验（默认true）
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return props;
    }

    /**
     * 给生产者添加配置
     *
     * @param bootStrapServers
     * @return
     */
    public KafkaProducerBuilder setProducer(String bootStrapServers) {
        Properties props = this.producerConfigs();
        this.kafkaProducer = new KafkaProducer<>(props);
        return this;
    }

    /**
     * 异步推送消息
     *
     * @param topic 主题
     * @param key   键
     * @param msg   消息内容
     */
    public void produce(String topic, String key, String msg) {
        log.info("topic: {}, key: {}, msg: {}", topic, key, msg);
        if (topic == null || topic.isEmpty()) {
            topic = KafkaConstant.DEFAULT_TOPIC;
        }
        if (key == null || key.isEmpty()) {
            key = Uuid.randomUuid().toString();
        }
        ProducerRecord<String, String> kafkaMessage = new ProducerRecord<>(topic, key, msg);
        // 异步发送方式：生产者调用 send() 方法后，可以传递一个回调函数，在消息发送完成后，会调用该回调函数，通过回调函数可以获取到发送结果
        kafkaProducer.send(kafkaMessage, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e != null) {
                    log.info("消息发送失败：{}", e.getMessage());
                } else {
                    log.info("消息发送成功，offset：{}", recordMetadata.offset());
                }
            }
        });
    }

    /**
     * 关闭生产者
     */
    public void close() {
        this.kafkaProducer.close();
    }
}
