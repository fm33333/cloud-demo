package cn.itcast.demo.controller;

import cn.itcast.demo.producer.BaseKafkaProducer;
import cn.itcast.demo.producer.TestKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducerController {

    @Deprecated
    @Autowired
    BaseKafkaProducer baseKafkaProducer;

    @Autowired
    TestKafkaProducer testKafkaProducer;


    @PostMapping("/produce")
    public String produce(@RequestParam String msg) {
        baseKafkaProducer.produce(msg);
        return "ok";
    }

    @PostMapping("/produceWithCallBack")
    public String produceWithCallBack(@RequestParam String msg) {
        baseKafkaProducer.produceWithCallBack(msg);
        return "ok";
    }

    @PostMapping("/testKafkaProducer")
    public String testKafkaProducer(@RequestParam String msg) {
        testKafkaProducer.produce(msg);
        return "ok";
    }

    @PostMapping("/produceWithTopicKeyValue")
    public String produceWithTopicKeyValue(@RequestParam String topic, @RequestParam String key, @RequestParam String msg) {
        testKafkaProducer.produce(topic, key, msg);
        return "ok";
    }

    @PostMapping("/produceWithTopicValue")
    public String produceWithTopicKeyValue(@RequestParam String topic, @RequestParam String msg) {
        testKafkaProducer.produce(topic, msg);
        return "ok";
    }

}
