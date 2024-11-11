package com.example.MediLink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendContactEmail(String name, String email, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("yc00526n@pace.edu");
        mailMessage.setSubject("Customer Inquiry: " + subject);
        mailMessage.setText("User Name: " + name + "\nEmail: " + email + "\n\nMessage:\n" + message);

        mailSender.send(mailMessage);
    }
}

