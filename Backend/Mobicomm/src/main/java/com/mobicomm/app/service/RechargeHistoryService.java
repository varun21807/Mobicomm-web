package com.mobicomm.app.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mobicomm.app.model.Plan;
import com.mobicomm.app.model.RechargeHistory;
import com.mobicomm.app.model.User;
import com.mobicomm.app.repository.PlanRepository;
import com.mobicomm.app.repository.RechargeHistoryRepository;
import com.mobicomm.app.repository.UserRepository;

@Service
public class RechargeHistoryService {

    @Autowired
    private RechargeHistoryRepository rechargeHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;
    
    @Autowired
    private JavaMailSender mailSender;

    // ✅ Get the currently authenticated user's phone number
    private String getAuthenticatedPhoneNumber() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // Directly use as a string
    }

    // ✅ Get Recharge History by Phone Number
    public List<RechargeHistory> getRechargeHistoryByPhoneNumber(String phoneNumber) {
        return rechargeHistoryRepository.findByPhoneNumber(phoneNumber);
    }

    // ✅ Add a new Recharge Record with Payment Status
    public RechargeHistory addRecharge(String planId, double amountPaid, String phoneNumber, String paymentMethod, String paymentStatus) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        RechargeHistory recharge = new RechargeHistory();
        recharge.setId("RECH-" + UUID.randomUUID().toString().substring(0, 8)); // ✅ Unique Recharge ID
        recharge.setPhoneNumber(phoneNumber);
        recharge.setPlan(plan);
        recharge.setRechargeDate(LocalDateTime.now());
        recharge.setAmount(amountPaid);
        recharge.setPaymentMethod(paymentMethod);
        recharge.setStatus(paymentStatus); // ✅ Store payment status from Razorpay API

        int validityDays = Math.max(plan.getValidity(), 1);
        recharge.setExpiryDate(recharge.getRechargeDate().plusDays(validityDays));

        return rechargeHistoryRepository.save(recharge);
    }

    // ✅ Fetch Users Whose Plans Expire in 7 Days
    public List<Map<String, Object>> getExpiringSubscribers() {
        LocalDateTime sevenDaysLater = LocalDateTime.now().plusDays(7);
        
        // Get expiring recharges
        List<RechargeHistory> expiringRecharges = rechargeHistoryRepository.findByExpiryDateBefore(sevenDaysLater);

        // Create a list to store combined results
        List<Map<String, Object>> expiringSubscribers = new ArrayList<>();

        for (RechargeHistory recharge : expiringRecharges) {
            String phoneNumber = recharge.getPhoneNumber();

            // Fetch user details using phone number
            Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                
                // ✅ Create response map
                Map<String, Object> subscriberInfo = new HashMap<>();
                subscriberInfo.put("phoneNumber", phoneNumber);
                subscriberInfo.put("userId", user.getUserId());
                subscriberInfo.put("userName", user.getUserName());
                subscriberInfo.put("userEmail", user.getUserEmail());
                subscriberInfo.put("expiryDate", recharge.getExpiryDate());

                expiringSubscribers.add(subscriberInfo);
            }
        }

        return expiringSubscribers;
    }
}
