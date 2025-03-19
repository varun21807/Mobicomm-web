package com.mobicomm.app.model;

import lombok.Data;

@Data
public class PaymentRequest {
    private String orderId;
    private Double order_amount;
    private String order_currency;
    private String customerPhone;
    private String customerEmail;
    private String customerName;
    private String returnUrl;  // ✅ Added
    private String notifyUrl;  // ✅ Added
    private String planId;
}
