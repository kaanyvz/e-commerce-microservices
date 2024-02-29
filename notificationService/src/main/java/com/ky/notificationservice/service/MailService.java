package com.ky.notificationservice.service;

import com.ky.rabbitservice.request.EmailRequest;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.eclipse.angus.mail.smtp.SMTPTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.Properties;

@Service
public class MailService {
    private final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Value("${mail.from}")
    private String from;

    @Value("${mail.password}")
    private String password;

    public void sendMail(EmailRequest request) {
        try {
            Message message = createNewMail(request);
            SMTPTransport smtpTransport = (SMTPTransport) getMailSession().getTransport("smtp");
            System.out.println(smtpTransport);
            smtpTransport.connect("smtp.gmail.com", from, password);
            smtpTransport.sendMessage(message, message.getAllRecipients());
            smtpTransport.close();
        }catch (MessagingException e){
            logger.error("Error sending email:", e);
            throw new RuntimeException("EMAIL DID NOT SEND...");
        }
    }

    private Message createNewMail(EmailRequest request) throws MessagingException {
        Message message = new MimeMessage(getMailSession());
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(request.getEmail(), false));
        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("", false));
        message.setSubject(request.getSubject());
        message.setText(request.getText());
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    private Session getMailSession(){
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.trust", "*");

        return Session.getInstance(properties, null);
    }
}
