package com.mobicomm.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender; // Ensure JavaMailSender is configured

    @Value("${spring.mail.username}")  // Check email sender in properties
    private String fromEmail;

    public void sendRechargeConfirmation(String email, String planId, double amount) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("Recharge Successful - Plan ID: " + planId);
            message.setText("Your recharge for Plan ID " + planId + " of ₹" + amount + " is successful!");

            javaMailSender.send(message);
            System.out.println("📧 Email Sent to " + email);

        } catch (Exception e) {
            System.err.println("❌ Email Sending Failed: " + e.getMessage());
        }
    }
}
