package cn.itcast.demo.producer;

import cn.itcast.demo.constant.KafkaConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

/**
 * Kafka producer（使用KafkaProducer推送消息，配置类构建Bean）
 * TODO：与KafkaConsumer的构建方式不同，应统一成一种方式
 */
@Slf4j
@Component
public class KafkaProducerHandler implements ProducerListener<Object, Object> {

    @Autowired
    KafkaProducer<String, String> kafkaProducer;

    /**
     * 推送消息
     * 根据判断RecordMetadata是否有值来确认消息是否发送成功
     * TODO：同步异步分成2个方法 or 加一个参数判断是否同步
     *
     * @param msg
     */
    public void produce(String msg) {
        log.info("msg: {}", msg);
        String key = Uuid.randomUuid().toString();
        ProducerRecord<String, String> kafkaMessage = new ProducerRecord<>(KafkaConstant.DEFAULT_TOPIC, key, msg);
        // 同步发送方式：有序发送消息，生产者调用 send() 方法后，会等待消息的确认返回，如果发送成功，send() 方法会返回一个 RecordMetadata 对象，其中包含了消息的元数据信息；如果发送失败，则可能抛出异常
        /*try {
            Future<RecordMetadata> metadataFuture = kafkaProducer.send(kafkaMessage);
            // 消息发送成功才有RecordMetadata值
            RecordMetadata metadata = metadataFuture.get();
            log.info("metadata: {}", metadata.toString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }*/
        // 异步发送方式：无序发送消息，生产者调用 send() 方法后，可以传递一个回调函数，在消息发送完成后，会调用该回调函数，通过回调函数可以获取到发送结果
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
     * 推送消息
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
     * 推送消息
     *
     * @param topic
     * @param msg
     */
    public void produce(String topic, String msg) {
        log.info("topic: {}, msg: {}", topic, msg);
        ProducerRecord<String, String> kafkaMessage = new ProducerRecord<>(topic, msg);
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

}
