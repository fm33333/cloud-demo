package cn.itcast.demo.controller;

import cn.itcast.demo.consumer.KafkaConsumerBuilder;
import cn.itcast.demo.producer.KafkaProducerBuilder;
import cn.itcast.demo.producer.KafkaTemplateHandler;
import cn.itcast.demo.producer.KafkaProducerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kafka生产消息控制类
 */
@RestController
public class KafkaProducerController {

    @Autowired
    KafkaTemplateHandler kafkaTemplateHandler;

    @Autowired
    KafkaProducerHandler kafkaProducerHandler;

    /**     KafkaTemplate      */

    /**
     * 同步推送消息（KafkaTemplate）
     *
     * @param msg
     * @return
     */
    @PostMapping("/produce")
    public String produce(@RequestParam String msg) {
        kafkaTemplateHandler.produce(msg);
        return "ok";
    }

    /**
     * 异步推送消息，带有回调（KafkaTemplate）
     *
     * @param msg
     * @return
     */
    @PostMapping("/produceWithCallBack")
    public String produceWithCallBack(@RequestParam String msg) {
        kafkaTemplateHandler.produceWithCallBack(msg);
        return "ok";
    }


    /**    KafkaProducer    */

    /**
     * 异步推送消息（KafkaProducer）
     *
     * @param topic
     * @param key
     * @param msg
     * @return
     */
    @PostMapping("/produceByKafkaProducer")
    public String produceByKafkaProducer(@RequestParam(required = false) String topic,
                                         @RequestParam(required = false) String key,
                                         @RequestParam String msg) {
        kafkaProducerHandler.produce(topic, key, msg);
        return "ok";
    }

    /**
     * 异步推送消息（KafkaProducerBuilder）
     *
     * @param topic
     * @param key
     * @param msg
     * @return
     */
    @PostMapping("/produceByKafkaProducerBuilder")
    public String produceByKafkaProducerBuilder(@RequestParam(required = false) String topic,
                                                @RequestParam(required = false) String key,
                                                @RequestParam String msg) {
        // TODO: 缓存producerBuilder
        KafkaProducerBuilder producerBuilder = KafkaProducerBuilder.build("");
        producerBuilder.produce(topic, key, msg);
        return "ok";
    }

}
