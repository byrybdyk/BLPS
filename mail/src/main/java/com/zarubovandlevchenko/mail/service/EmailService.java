package com.zarubovandlevchenko.mail.service;

import com.zarubovandlevchenko.mail.DTO.MailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

   @KafkaListener(topics = "mail-topic", groupId = "mail-consumer-group")
    public void sendEmail(MailMessage message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(message.getTo());
        mail.setSubject(message.getSubject());
        mail.setText(message.getBody());

        mailSender.send(mail);
        System.out.println("Email sent to: " + message.getTo());
    }
}

