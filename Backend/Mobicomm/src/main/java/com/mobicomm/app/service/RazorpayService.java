package com.mobicomm.app.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mobicomm.app.model.ActivePlan;
import com.mobicomm.app.model.Payment;
import com.mobicomm.app.model.PaymentRequest;
import com.mobicomm.app.model.PaymentResponse;
import com.mobicomm.app.model.Plan;
import com.mobicomm.app.model.Purchase;
import com.mobicomm.app.model.RechargeHistory;
import com.mobicomm.app.model.User;
import com.mobicomm.app.repository.ActivePlanRepository;
import com.mobicomm.app.repository.PaymentRepository;
import com.mobicomm.app.repository.PlanRepository;
import com.mobicomm.app.repository.PurchaseRepository;
import com.mobicomm.app.repository.RechargeHistoryRepository;
import com.mobicomm.app.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CashfreeWebhookConfig {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RechargeHistoryRepository rechargeHistoryRepository;

    @Autowired
    private ActivePlanRepository activePlanRepository;

    @Autowired
    private IdGeneratorUtil idGeneratorUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmailService emailService; 

    // Generate a unique Purchase ID (PUR-YYYYMMDD-XXXX)
    private String generatePurchaseId() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = String.format("%04d", new Random().nextInt(10000));
        return "PUR-" + datePart + "-" + randomPart;
    }

    /**
     * Handle Cashfree Webhook for Payment Completion
     */
    @Transactional(rollbackOn = Exception.class)
    public String handlePaymentWebhook(String phoneNumber, String planId, double amount, String transactionId) {
        System.out.println("📌 Webhook received for phoneNumber: " + phoneNumber + ", planId: " + planId);

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Phone number not found. Please register first."));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        String purchaseId = generatePurchaseId();

        // ✅ Save Purchase Record
        Purchase purchase = new Purchase();
        purchase.setPurchaseId(purchaseId);
        purchase.setPhoneNumber(phoneNumber);
        purchase.setPlan(plan);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setExpiryDate(LocalDateTime.now().plusDays(plan.getValidity()));

        purchaseRepository.save(purchase);

        // ✅ Save Payment Record
        Payment payment = new Payment();
        payment.setTransactionId(transactionId);
        payment.setPurchase(purchase);
        payment.setAmount(plan.getPrice());
        payment.setStatus("SUCCESS");
        payment.setTimestamp(LocalDateTime.now());

        paymentRepository.save(payment);

        // ✅ Save Recharge History
        RechargeHistory rechargeHistory = new RechargeHistory();
        rechargeHistory.setId(idGeneratorUtil.generateRechargeHistoryId());
        rechargeHistory.setPhoneNumber(phoneNumber);
        rechargeHistory.setPlan(plan);
        rechargeHistory.setRechargeDate(LocalDateTime.now());
        rechargeHistory.setAmount(plan.getPrice());

        rechargeHistoryRepository.save(rechargeHistory);

        // ✅ Manage Active Plans
        List<ActivePlan> existingActivePlans = activePlanRepository.findByPhoneNumber(phoneNumber);
        for (ActivePlan activePlan : existingActivePlans) {
            if (activePlan.getPlan().getPlanId().equals(planId)) {
                LocalDateTime newExpiry = LocalDateTime.now().plusDays(plan.getValidity());

                if (newExpiry.isAfter(activePlan.getExpiryDate())) {
                    activePlan.setExpiryDate(newExpiry);
                    activePlanRepository.save(activePlan);
                }

                sendRechargeEmail(user.getUserEmail(), planId, plan.getPrice());
                return "Purchase Completed via Webhook";
            }
        }

        // ✅ If No Existing Plan, Create New Active Plan
        ActivePlan activePlan = new ActivePlan();
        activePlan.setId(idGeneratorUtil.generateActivePlanId());
        activePlan.setPhoneNumber(phoneNumber);
        activePlan.setPlan(plan);
        activePlan.setActivationDate(LocalDateTime.now());
        activePlan.setExpiryDate(LocalDateTime.now().plusDays(plan.getValidity()));

        activePlanRepository.save(activePlan);

        // ✅ Send Email Notification
        sendRechargeEmail(user.getUserEmail(), planId, plan.getPrice());

        return "Purchase Completed via Webhook";
    }

    private void sendRechargeEmail(String email, String planId, double amount) {
        if (email != null && !email.isEmpty()) {
            System.out.println("📩 Preparing to send email to: " + email);
            emailService.sendRechargeConfirmation(email, planId, amount);
            System.out.println("✅ Email Sent Successfully!");
        } else {
            System.err.println("⚠ No email found for user. Skipping email notification.");
        }
    }

    // ✅ Fetch Recharge History
    public List<RechargeHistory> getRechargeHistory(String phoneNumber) {
        return rechargeHistoryRepository.findByPhoneNumber(phoneNumber);
    }

    // ✅ Fetch Active Plans
    public List<ActivePlan> getActivePlans(String phoneNumber) {
        return activePlanRepository.findByPhoneNumber(phoneNumber);
    }
}
