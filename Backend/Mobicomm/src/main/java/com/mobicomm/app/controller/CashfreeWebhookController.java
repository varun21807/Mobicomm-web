package com.mobicomm.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mobicomm.app.service.CashfreeWebhookConfig;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class CashfreeWebhookController {

    @Autowired
    private CashfreeWebhookConfig cashfreeWebhookConfig;

    @PostMapping("/webhook")
    public ResponseEntity<String> handlePaymentWebhook(@RequestBody Map<String, Object> payload) {
        try {
            String status = (String) payload.get("order_status");
            String planId = (String) payload.get("plan_id");
            String phoneNumber = (String) payload.get("customer_phone");
            double amountPaid = Double.parseDouble(payload.get("order_amount").toString());
            String transactionId = (String) payload.get("cf_payment_id");

            if (!"PAID".equalsIgnoreCase(status)) {
                return ResponseEntity.badRequest().body("Payment not successful");
            }

            String result = cashfreeWebhookConfig.handlePaymentWebhook(phoneNumber, planId, amountPaid, transactionId);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing payment: " + e.getMessage());
        }
    }
}
