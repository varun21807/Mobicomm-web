package com.mobicomm.app.service;

import com.razorpay.Order;
import com.razorpay.Payment;
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

    /**
     * ✅ Create Razorpay Order
     */
    public String createOrder(double amount) throws Exception {
        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
        
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int) (amount * 100));  // ✅ Convert to paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_" + UUID.randomUUID().toString().substring(0, 8));
        orderRequest.put("payment_capture", 1); // Auto-capture payment
        
        Order order = razorpay.orders.create(orderRequest);
        return order.toString();
    }

    public void storeRechargeHistory(String phoneNumber, String planId, double amount, String transactionId) {
        try {
            // ✅ Log transaction ID
            System.out.println("🔍 Received Transaction ID: " + transactionId);

            // ✅ Validate transaction ID before calling Razorpay API
            if (transactionId == null || transactionId.trim().isEmpty() || !transactionId.startsWith("pay_")) {
                throw new RuntimeException("❌ Invalid Transaction ID: " + transactionId);
            }

            // ✅ Fetch payment details from Razorpay
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
            com.razorpay.Payment payment = razorpay.payments.fetch(transactionId);

            if (payment == null) {
                throw new RuntimeException("❌ Razorpay returned null for transaction ID: " + transactionId);
            }

            // ✅ Extract details from Razorpay response
            String actualPaymentMethod = payment.get("method");
            String paymentStatus = payment.get("status");
            int razorpayAmountPaise = ((Number) payment.get("amount")).intValue(); // Convert Object to int
            double razorpayAmount = razorpayAmountPaise / 100.0; // Convert paise to rupees

            System.out.println("✅ Payment Details Retrieved: " + payment.toString());

            // ✅ Verify that the amount matches
            if (razorpayAmount != amount) {
                throw new RuntimeException("❌ Payment amount mismatch! Expected: " + amount + " rupees, Received: " + razorpayAmount + " rupees");
            }

            // ✅ Fetch user from database
            User user = userRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new RuntimeException("❌ User not found for phone: " + phoneNumber));

            // ✅ Fetch plan details
            Plan plan = planRepository.findById(planId)
                    .orElseThrow(() -> new RuntimeException("❌ Plan not found for ID: " + planId));

            // ✅ Create new recharge history entry
            RechargeHistory recharge = new RechargeHistory();
            recharge.setId("RECH-" + UUID.randomUUID().toString().substring(0, 8));
            recharge.setPhoneNumber(phoneNumber);
            recharge.setPlan(plan);
            recharge.setRechargeDate(LocalDateTime.now());
            recharge.setAmount(amount);
            recharge.setPaymentMethod(actualPaymentMethod);
            recharge.setStatus(paymentStatus);

            // ✅ Ensure plan validity is handled correctly
            int validityDays = (plan.getValidity() != null) ? Math.max(plan.getValidity(), 1) : 1;
            recharge.setExpiryDate(recharge.getRechargeDate().plusDays(validityDays));

            System.out.println("📡 Storing Recharge History in DB...");
            rechargeHistoryRepository.save(recharge);
            System.out.println("✅ Recharge History Stored Successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error in storeRechargeHistory: " + e.getMessage());
            throw new RuntimeException("❌ Failed to fetch payment details from Razorpay: " + e.getMessage());
        }
    }

}
