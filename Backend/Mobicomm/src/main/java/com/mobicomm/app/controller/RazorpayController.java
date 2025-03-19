package com.mobicomm.app.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.mobicomm.app.service.RazorpayService;

@CrossOrigin(origins = {"http://127.0.0.1:5503", "http://localhost:5503"})
@RestController
@RequestMapping("/api/payment")
public class RazorpayController {

    @Autowired
    private RazorpayService razorpayService;

    @PostMapping("/create-order")
    public String createOrder(@RequestParam double amount) {
        try {
            return razorpayService.createOrder(amount * 100);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    private static final String RAZORPAY_KEY_ID = "rzp_test_7jbN2F87afR6Hf";
    private static final String RAZORPAY_SECRET = "fRGO8y1UD7OT33jN2oV3n3HN";

    @GetMapping("/payment-details/{paymentId}")
    public ResponseEntity<String> getPaymentDetails(@PathVariable String paymentId) {
        String apiUrl = "https://api.razorpay.com/v1/payments/" + paymentId;

        HttpHeaders headers = new HttpHeaders();
        String auth = Base64.getEncoder().encodeToString((RAZORPAY_KEY_ID + ":" + RAZORPAY_SECRET).getBytes());
        headers.setBasicAuth(auth);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        return response;
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<Map<String, Object>> verifyPayment(@RequestBody Map<String, String> paymentData) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("Received Payment Data: " + paymentData);

            String phoneNumber = paymentData.get("mobileNumber");  
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Phone number is missing");
                return ResponseEntity.badRequest().body(response);
            }

            String planId = paymentData.get("planId");
            if (planId == null || planId.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Plan ID is missing");
                return ResponseEntity.badRequest().body(response);
            }

            String paymentMethod = paymentData.get("paymentMethod");
            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Payment method is missing");
                return ResponseEntity.badRequest().body(response);
            }

            double amount;
            try {
                amount = Double.parseDouble(paymentData.get("amount"));
            } catch (NumberFormatException e) {
                response.put("status", "error");
                response.put("message", "Invalid amount format");
                return ResponseEntity.badRequest().body(response);
            }

            // Debugging user lookup
            System.out.println("🔍 Checking user in DB for phone: " + phoneNumber);

            // Store recharge history with payment method
            razorpayService.storeRechargeHistory(phoneNumber, planId, amount, paymentMethod);

            // ✅ SUCCESS RESPONSE IN JSON FORMAT
            response.put("status", "success");
            response.put("message", "Payment successful, recharge stored!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "❌ Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


}
