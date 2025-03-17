//package com.mobicomm.app.service;
//
//import com.mobicomm.app.model.Payment;
//import com.mobicomm.app.repository.PaymentRepository;
//import org.springframework.stereotype.Service;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//
//@Service
//public class PaymentService {
//
//    private final PaymentRepository paymentRepository;
//
//    public PaymentService(PaymentRepository paymentRepository) {
//        this.paymentRepository = paymentRepository;
//    }
//
//    public Payment savePayment(Payment payment) {
//        // Generate a custom Payment ID before saving
//        payment.setTransactionId(generatePaymentId());
//        return paymentRepository.save(payment);
//    }
//
//    public List<Payment> getAllPayments() {
//        return paymentRepository.findAll();
//    }
//
//    public Payment getPaymentById(String paymentId) {
//        return paymentRepository.findById(paymentId).orElse(null);
//    }
//
//    public List<Payment> getPaymentsByPurchaseId(String purchaseId) {
//        return paymentRepository.findByPurchase_PurchaseId(purchaseId);
//    }
//
//    private String generatePaymentId() {
//        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
//        int randomPart = new Random().nextInt(9000) + 1000; // Random 4-digit number
//        return "PAY-" + datePart + "-" + randomPart;
//    }
//}
