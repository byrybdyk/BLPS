//package com.zarubovandlevchenko.mail.service;
//
//import com.zarubovandlevchenko.mail.DTO.MailMessage;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class StartupMailSender {
//
//    private final EmailService emailService;
//
//    @PostConstruct
//    public void testEmail() {
//        SimpleMailMessage mail = new SimpleMailMessage();
//        mail.setTo("your@email.com");
//        mail.setSubject("Startup test");
//        mail.setText("Test email after app start");
//
//        emailService.sendEmail(mail);
//    }
//}
//
