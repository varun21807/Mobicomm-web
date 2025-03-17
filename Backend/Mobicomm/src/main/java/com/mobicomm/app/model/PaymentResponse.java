package com.mobicomm.app.model;

import lombok.Data;

@Data
public class PaymentResponse {
    private String payment_session_id;
    private String status;
}
