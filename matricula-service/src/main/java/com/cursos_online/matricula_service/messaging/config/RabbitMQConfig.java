package com.cursos_online.matricula_service.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "usuario.exchange";

    public static final String QUEUE_MATRICULA_REALIZADA = "usuario.matricula.realizada";

    public static final String ROUTING_KEY_REALIZADA = "usuario.rota.matricula.realizada";

    @Bean
    public Queue queueMatriculaRealizada() {
        return new Queue(QUEUE_MATRICULA_REALIZADA, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding bindingRealizada() {
        return BindingBuilder.bind(queueMatriculaRealizada())
                .to(exchange())
                .with(ROUTING_KEY_REALIZADA);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
