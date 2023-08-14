package com.project.ecommerce.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private final String directExchangeName = "ecommerce-direct-exchange";
    private final String fanoutExchangeName = "ecommerce-fanout-exchange";
    private final String topicExchangeName = "ecommerce-topic-exchange";

    @Bean
    public Queue topicQueue() {
        return new Queue("ecommerce.topic.queue", true, false, false);
    }

    @Bean
    public Queue fanoutQueue() {
        return new Queue("ecommerce.fanout.queue", true, false, false);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory, TopicExchange topicExchange, FanoutExchange fanoutExchange, DirectExchange directExchange) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareBinding(BindingBuilder.bind(topicQueue()).to(topicExchange).with("ecommerce.topic.key.*"));
        rabbitAdmin.declareBinding(BindingBuilder.bind(fanoutQueue()).to(fanoutExchange));
        return rabbitAdmin;
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(directExchangeName);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(fanoutExchangeName);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }
}
