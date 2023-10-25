package cn.itcast.user.rabbitmq.config;

import cn.itcast.user.rabbitmq.constant.RabbitMQConstant;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq 生产者配置类
 */
@Slf4j
@Configuration
public class RabbitConfig {

    /**
     * direct队列 起名：testDirectQueue
     *
     * @return
     */
    @Bean
    public Queue testDirectQueue() {
        return new Queue(RabbitMQConstant.DIRECT_NAME, true);
    }

    /**
     * direct交换机 起名：testDirectExchange
     *
     * @return
     */
    @Bean
    DirectExchange testDirectExchange() {
        return new DirectExchange(RabbitMQConstant.DIRECT_NAME, true, false);
    }

    /**
     * 绑定队列和交换机，设置匹配键testDirectRouting
     * @return
     */
    @Bean
    Binding bindingDirect() {
        return BindingBuilder
                .bind(testDirectQueue())
                .to(testDirectExchange())
                .with(RabbitMQConstant.DIRECT_ROUTING_KEY);
    }


    /*@Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);

        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("ConfirmCallback: 相关数据");
            }
        });

        return rabbitTemplate;
    }*/
}