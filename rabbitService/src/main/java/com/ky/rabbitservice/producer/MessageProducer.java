package com.ky.rabbitservice.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {
    private final AmqpTemplate amqpTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);

    public MessageProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void publish(Object payload, String exchange, String routingKey){
        LOGGER.info("Publishing to {} using routingKey: {}, Payload: {}", exchange, routingKey,payload);
        amqpTemplate.convertAndSend(exchange, routingKey, payload);
        LOGGER.info("Published to {} using routingKey: {}, Payload: {}", exchange, routingKey,payload);

    }
}
