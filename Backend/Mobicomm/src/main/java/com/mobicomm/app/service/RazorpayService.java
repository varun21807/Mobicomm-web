package com.mobicomm.app.service;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mobicomm.app.model.Plan;
import com.mobicomm.app.model.RechargeHistory;
import com.mobicomm.app.model.User;
import com.mobicomm.app.repository.PlanRepository;
import com.mobicomm.app.repository.RechargeHistoryRepository;
import com.mobicomm.app.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RazorpayService {
    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    @Autowired
    private RechargeHistoryRepository rechargeHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;
    
    @Autowired
    private EmailService emailService;

    public String createOrder(double amount) throws Exception {
        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
        
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int) (amount));
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_" + UUID.randomUUID().toString().substring(0, 8));
        orderRequest.put("payment_capture", 1); // Auto-capture payment
        
        Order order = razorpay.orders.create(orderRequest);
        return order.toString();
    }
    public void storeRechargeHistory(String phoneNumber, String planId, double amount, String paymentMethod) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        RechargeHistory recharge = new RechargeHistory();
        recharge.setId("RECH-" + UUID.randomUUID().toString().substring(0, 8));
        recharge.setPhoneNumber(phoneNumber);
        recharge.setPlan(plan);
        recharge.setRechargeDate(LocalDateTime.now());
        recharge.setAmount(amount);
        recharge.setPaymentMethod(paymentMethod);

        // Ensure validity is not 0
        int validityDays = plan.getValidity() > 0 ? plan.getValidity() : 1; // Default to 1 day if validity is 0
        LocalDateTime expiryDate = recharge.getRechargeDate().plusDays(validityDays);
        recharge.setExpiryDate(expiryDate);

        rechargeHistoryRepository.save(recharge);
    }

}
