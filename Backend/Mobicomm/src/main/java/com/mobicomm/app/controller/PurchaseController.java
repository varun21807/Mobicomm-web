//package com.mobicomm.app.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.mobicomm.app.model.ActivePlan;
//import com.mobicomm.app.model.Purchase;
//import com.mobicomm.app.model.RechargeHistory;
//import com.mobicomm.app.service.PurchaseService;
//
//@RestController
//@RequestMapping("/api/purchase")
//public class PurchaseController {
//
//    @Autowired
//    private PurchaseService purchaseService;
//
//    // Recharge without login
//    @PostMapping("/new")
//    public ResponseEntity<?> recharge(@RequestParam String phoneNumber, @RequestParam String planId) {
//        try {
//            Purchase purchase = purchaseService.createPurchaseWithoutLogin(phoneNumber, planId);
//            return ResponseEntity.ok(purchase);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//    // Get Recharge History (Requires Login)
//    @GetMapping("/history")
//    public ResponseEntity<List<RechargeHistory>> getRechargeHistory(Authentication authentication) {
//        String phoneNumber = authentication.getName(); // Extract phone from JWT
//        return ResponseEntity.ok(purchaseService.getRechargeHistory(phoneNumber));
//    }
//
//    // Get Active Plans (Requires Login)
//    @GetMapping("/active-plans")
//    public ResponseEntity<List<ActivePlan>> getActivePlans(Authentication authentication) {
//        String phoneNumber = authentication.getName(); // Extract phone from JWT
//        return ResponseEntity.ok(purchaseService.getActivePlans(phoneNumber));
//    }
//}