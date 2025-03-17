package com.mobicomm.app.model;



import lombok.Data;

import java.util.Map;

@Data
public class PaymentRequest {
    private String orderId;
    private double orderAmount;
    private String orderCurrency;
    private Map<String, String> customerDetails;
}
