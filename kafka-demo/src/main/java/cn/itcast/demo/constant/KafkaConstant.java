package cn.itcast.demo.constant;

/**
 * 常量类
 */
public class KafkaConstant {

    // 集群节点
    public static final String BOOTSTRAP_SERVERS = "192.168.253.129:19092,192.168.253.129:29092,192.168.253.129:39092";
    // key和value的序列化和反序列化
    public static final String STRING_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String STRING_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";


    public static final String DEFAULT_TOPIC = "topic.1";
    public static final String COLOR_TOPIC = "color";

    public static final String GROUP_ID_0 = "kafka-test-0";
    public static final String GROUP_ID_1 = "kafka-test-1";
    public static final String GROUP_ID_2 = "kafka-test-2";

}
