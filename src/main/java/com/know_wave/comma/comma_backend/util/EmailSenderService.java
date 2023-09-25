package com.know_wave.comma.comma_backend.util;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final MailProperties properties;
    private final JavaMailSender mailSender;

    public EmailSenderService(MailProperties properties, JavaMailSender mailSender) {
        this.properties = properties;
        this.mailSender = mailSender;
    }

    public void send(String email, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(properties.getUsername());
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
