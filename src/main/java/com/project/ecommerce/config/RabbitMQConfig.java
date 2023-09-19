package com.project.ecommerce.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue topicQueue() {
        return new Queue("ecommerce.topic.queue", true, false, false);
    }

    @Bean
    public Queue fanoutQueue() {
        return new Queue("ecommerce.fanout.queue", true, false, false);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory, TopicExchange topicExchange, FanoutExchange fanoutExchange) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareBinding(BindingBuilder.bind(topicQueue()).to(topicExchange).with("ecommerce.topic.key.*"));
        rabbitAdmin.declareBinding(BindingBuilder.bind(fanoutQueue()).to(fanoutExchange));
        return rabbitAdmin;
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("localhost");
    }

    @Bean
    public DirectExchange directExchange() {
        String directExchangeName = "ecommerce-direct-exchange";
        return new DirectExchange(directExchangeName);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        String fanoutExchangeName = "ecommerce-fanout-exchange";
        return new FanoutExchange(fanoutExchangeName);
    }

    @Bean
    public TopicExchange topicExchange() {
        String topicExchangeName = "ecommerce-topic-exchange";
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
