package com.mobicomm.app.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.mobicomm.app.service.RazorpayService;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;

@CrossOrigin(origins = {"http://127.0.0.1:5503", "http://localhost:5503"})
@RestController
@RequestMapping("/api/payment")
public class RazorpayController {

	 @Value("${razorpay.api.key}")
	    private String apiKey;

	    @Value("${razorpay.api.secret}")
	    private String apiSecret;
	    
    @Autowired
    private RazorpayService razorpayService;
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestParam long amount) {  // ✅ Use long instead of double
        try {
            String order = razorpayService.createOrder(amount); // ✅ No extra * 100
            return ResponseEntity.ok(new JSONObject(order).toString());
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create order");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    private static final String RAZORPAY_KEY_ID = "rzp_test_7jbN2F87afR6Hf";
    private static final String RAZORPAY_SECRET = "fRGO8y1UD7OT33jN2oV3n3HN";

    @GetMapping("/details/{paymentId}")
    public Map<String, Object> getPaymentDetails(@PathVariable String paymentId) {
        try {
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
            Payment payment = razorpay.payments.fetch(paymentId);

            Map<String, Object> paymentDetails = new HashMap<>();
            paymentDetails.put("id", payment.get("id"));
            paymentDetails.put("amount", payment.get("amount"));
            paymentDetails.put("currency", payment.get("currency"));
            paymentDetails.put("status", payment.get("status"));
            paymentDetails.put("method", payment.get("method"));
            paymentDetails.put("order_id", payment.get("order_id"));

            return paymentDetails;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch payment details: " + e.getMessage());
        }
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

            String transactionId = paymentData.get("transactionId"); // ✅ Fix: Use transactionId, NOT paymentMethod
            if (transactionId == null || transactionId.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Transaction ID is missing");
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

            // Debugging logs
            System.out.println("🔍 Checking user in DB for phone: " + phoneNumber);
            System.out.println("🔍 Verifying payment for Transaction ID: " + transactionId);

            // ✅ Fix: Pass transactionId instead of paymentMethod
            razorpayService.storeRechargeHistory(phoneNumber, planId, amount, transactionId);

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