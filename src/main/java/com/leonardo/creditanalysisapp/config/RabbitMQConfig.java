package com.leonardo.creditanalysisapp.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RabbitMQConfig {

    private static final double BACKOFF_MULTIPLIER = 3.0;
    private static final long INITIAL_INTERVAL = 2000;
    private static final long MAX_INTERVAL = 50000;

    @Value("${spring.rabbitmq.host:localhost}")
    private String host;

    @Value("${spring.rabbitmq.port:5672}")
    private int port;

    @Value("${spring.rabbitmq.username:guest}")
    private String username;

    @Value("${spring.rabbitmq.password:guest}")
    private String password;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(INITIAL_INTERVAL);
        backOffPolicy.setMultiplier(BACKOFF_MULTIPLIER);
        backOffPolicy.setMaxInterval(MAX_INTERVAL);

        retryTemplate.setBackOffPolicy(backOffPolicy);

        log.info("Configured retry template with exponential backoff (multiplier: {})", BACKOFF_MULTIPLIER);
        return retryTemplate;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setRetryTemplate(retryTemplate());
        return rabbitTemplate;
    }

    /**
     * Customizes any retry templates created by Spring AMQP auto-configuration
     */
    @Bean
    public RabbitRetryTemplateCustomizer rabbitRetryTemplateCustomizer() {
        return (target, retryTemplate) -> {
            ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
            backOffPolicy.setInitialInterval(INITIAL_INTERVAL);
            backOffPolicy.setMultiplier(BACKOFF_MULTIPLIER);
            backOffPolicy.setMaxInterval(MAX_INTERVAL);

            retryTemplate.setBackOffPolicy(backOffPolicy);
            log.info("Customized auto-configured retry template with multiplier: {}", BACKOFF_MULTIPLIER);
        };
    }
}
