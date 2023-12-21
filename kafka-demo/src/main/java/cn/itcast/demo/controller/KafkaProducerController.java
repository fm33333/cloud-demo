package cn.itcast.demo.controller;

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

    /**
     * 异步推送消息（KafkaProducer）
     *
     * @param msg
     * @return
     */
    @Deprecated
    @PostMapping("/testKafkaProducer")
    public String testKafkaProducer(@RequestParam String msg) {
        kafkaProducerHandler.produce(msg);
        return "ok";
    }

    /**
     * 异步推送消息（KafkaProducer）
     *
     * @param topic
     * @param msg
     * @return
     */
    @Deprecated
    @PostMapping("/produceWithTopicValue")
    public String produceWithTopicKeyValue(@RequestParam String topic, @RequestParam String msg) {
        kafkaProducerHandler.produce(topic, msg);
        return "ok";
    }

    /**
     * 异步推送消息（KafkaProducer）
     *
     * @param topic
     * @param key
     * @param msg
     * @return
     */
    @PostMapping("/produceWithTopicKeyValue")
    public String produceWithTopicKeyValue(@RequestParam(required = false) String topic, @RequestParam(required = false) String key, @RequestParam String msg) {
        kafkaProducerHandler.produce(topic, key, msg);
        return "ok";
    }


}
