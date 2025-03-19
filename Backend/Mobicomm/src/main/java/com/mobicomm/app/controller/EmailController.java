package com.mobicomm.app.controller;

import com.mobicomm.app.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/email")
@CrossOrigin(origins = {"http://127.0.0.1:5503", "http://localhost:5503"})
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, Object> requestBody) {
        try {
            String to = (String) requestBody.get("to");
            String subject = (String) requestBody.get("subject");
            String body = (String) requestBody.get("body");

            emailService.sendHtmlEmail(to, subject, body);
            return ResponseEntity.ok("HTML Email sent successfully!");

        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Error sending email: " + e.getMessage());
        }
    }
}
