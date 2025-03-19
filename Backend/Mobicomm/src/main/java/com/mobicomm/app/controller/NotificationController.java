package com.mobicomm.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.internet.MimeMessage;
import java.util.Map;
@CrossOrigin(origins = {"http://127.0.0.1:5503", "http://localhost:5503"})
@RestController
@RequestMapping("/api")
public class NotificationController {

    private final JavaMailSender mailSender;

    public NotificationController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @PostMapping("/send-notification")
    public ResponseEntity<String> sendCustomNotification(@RequestBody Map<String, String> request) {
        try {
            String toEmail = request.get("toEmail");
            String subject = request.get("subject");
            String htmlContent = request.get("htmlContent");

            System.out.println("Attempting to send email to: " + toEmail); // Log email

            if (toEmail == null || toEmail.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Recipient email is missing!");
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // HTML content

            mailSender.send(message);
            System.out.println("Email sent successfully to " + toEmail);

            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error sending email: " + e.getMessage());
        }
    }


}
