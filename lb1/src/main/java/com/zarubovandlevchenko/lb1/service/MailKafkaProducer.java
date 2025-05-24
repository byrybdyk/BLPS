package com.zarubovandlevchenko.lb1.service;

import com.zarubovandlevchenko.lb1.dto.MailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailKafkaProducer {

    private final KafkaTemplate<String, MailMessage> kafkaTemplate;

    public void sendEmailMessage(MailMessage message) {
        kafkaTemplate.send("mail-topic", message);
        System.out.println("Сообщение отправлено в Kafka: " + message);
    }
}
