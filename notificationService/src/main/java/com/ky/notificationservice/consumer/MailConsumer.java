package com.ky.notificationservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ky.notificationservice.service.MailService;
import com.ky.rabbitservice.request.EmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class MailConsumer {
    private final MailService mailService;
    private final Logger logger = LoggerFactory.getLogger(MailConsumer.class);

    public MailConsumer(MailService mailService) {
        this.mailService = mailService;
    }

    @RabbitListener(queues = "${rabbitmq.queues.send-email}")
    public void sendMailConsumer(byte[] messageBytes){
        try {

            String message = new String(messageBytes, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            EmailRequest emailRequest = mapper.readValue(message, EmailRequest.class);

            logger.info("Consumed {} from send-email queue", emailRequest);
            mailService.sendMail(emailRequest);
        } catch (Exception e) {
            logger.error("Error processing email request: {}", e.getMessage());

        }
    }
}
