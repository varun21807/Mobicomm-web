package com.mobicomm.app.controller;

import com.mobicomm.app.model.RechargeHistory;
import com.mobicomm.app.repository.RechargeHistoryRepository;
import com.mobicomm.app.service.RazorpayService;
import com.mobicomm.app.service.RechargeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = {"http://127.0.0.1:5503", "http://localhost:5503"})
@RestController
@RequestMapping("/api/recharge-history")
public class RechargeHistoryController {

    @Autowired
    private RechargeHistoryService rechargeHistoryService;
    
    @Autowired
    private RazorpayService razorpayService;

    // Fetch recharge history by phone number
    @GetMapping("/{phoneNumber}")
    public ResponseEntity<?> getRechargeHistoryByPhone(@PathVariable String phoneNumber) {
        System.out.println("📡 Fetching recharge history for: " + phoneNumber);
        List<RechargeHistory> rechargeHistory = rechargeHistoryService.getRechargeHistoryByPhoneNumber(phoneNumber);

        if (rechargeHistory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No recharge history found for phone number: " + phoneNumber);
        }
        return ResponseEntity.ok(rechargeHistory);
    }
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<Map<String, Object>>> getExpiringSubscribers() {
        return ResponseEntity.ok(rechargeHistoryService.getExpiringSubscribers());
    }
    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> request) {
        String transactionId = request.get("transactionId");
        String phoneNumber = request.get("mobileNumber");
        String planId = request.get("planId");
        double amount = Double.parseDouble(request.get("amount"));

        if (transactionId == null || phoneNumber == null || planId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Missing required payment details.");
        }

        try {
            razorpayService.storeRechargeHistory(phoneNumber, planId, amount, transactionId);
            return ResponseEntity.ok(Map.of("message", "Payment verified and recharge stored successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error verifying payment: " + e.getMessage());
        }
    }



}
