package com.ky.notificationservice.consumer;

import com.ky.notificationservice.service.MailService;
import com.ky.rabbitservice.request.EmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MailConsumer {
    private final MailService mailService;
    private final Logger logger = LoggerFactory.getLogger(MailConsumer.class);

    public MailConsumer(MailService mailService) {
        this.mailService = mailService;
    }

    @RabbitListener(queues = "${rabbitmq.queues.send-email}")
    public void sendMailConsumer(EmailRequest request){
        logger.info("Consumed {} from send-email queue", request);
        mailService.sendMail(request);
    }
}
