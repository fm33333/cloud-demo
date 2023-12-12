package cn.itcast.demo.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Kafka配置类
 */
@Configuration
public class KafkaConfig {

    // 集群节点
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    // key和value的序列化
    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;
    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    /**
     * kafka生产者工厂
     *
     * @return
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * kafka生产者配置
     *
     * @return
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 30 * 1000);
        props.put(ProducerConfig.RETRIES_CONFIG, 5);
        props.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, 3000);
        return props;
    }

    /**
     * kafka生产者模板
     *
     * @return
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        // 设置监听器
        kafkaTemplate.setProducerListener(new ProducerListener<String, Object>() {
            @Override
            public void onSuccess(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata) {
                ProducerListener.super.onSuccess(producerRecord, recordMetadata);
                System.out.println("生产者监听器-消息发送成功：" + producerRecord.toString());
            }

            @Override
            public void onError(ProducerRecord<String, Object> producerRecord, Exception exception) {
                ProducerListener.super.onError(producerRecord, exception);
                System.out.println("生产者监听器-消息发送失败：" + producerRecord.toString());
                System.out.println(exception.getMessage());
            }
        });
        return kafkaTemplate;
    }

    /**
     * @return
     */
    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        return new KafkaProducer<>(producerConfigs());
    }
}
