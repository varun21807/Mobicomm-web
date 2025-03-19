package com.mobicomm.app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

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

    // Get the currently authenticated user's phone number
    private String getAuthenticatedPhoneNumber() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // Directly use as a string
    }

    // Get Recharge History by Phone Number
    public List<RechargeHistory> getRechargeHistoryByPhoneNumber(String phoneNumber) {
        return rechargeHistoryRepository.findByPhoneNumber(phoneNumber);
    }

    // Add a new Recharge Record
    public RechargeHistory addRecharge(String planId, double amountPaid, String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        RechargeHistory recharge = new RechargeHistory();
        recharge.setId("RECH-" + UUID.randomUUID().toString().substring(0, 8));
        recharge.setPhoneNumber(phoneNumber);
        recharge.setId(user.getUserId());
        recharge.setPlan(plan);
        recharge.setRechargeDate(LocalDateTime.now());
        recharge.setAmount(amountPaid);
        recharge.setPaymentMethod("Razorpay");

        return rechargeHistoryRepository.save(recharge);
    }
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
                
                // Create a response map
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
