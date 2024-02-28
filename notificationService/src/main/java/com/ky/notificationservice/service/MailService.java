package com.ky.notificationservice.service;

import com.ky.rabbitservice.request.EmailRequest;
import com.sun.mail.smtp.SMTPTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
public class MailService {

    @Value("${mail.from}")
    private String from;

    @Value("${mail.password}")
    private String password;

    public void sendMail(EmailRequest request) {
        try {
            Message message = createNewMail(request);
            SMTPTransport smtpTransport = (SMTPTransport) getMailSession().getTransport("smtps");
            smtpTransport.connect("smtp.gmail.com", from, password);
            smtpTransport.sendMessage(message, message.getAllRecipients());
            smtpTransport.close();
        }catch (MessagingException e){
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
        properties.put("mail.smtp.port", 465);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.starttls.required", null);
        return Session.getInstance(properties, null);
    }
}
