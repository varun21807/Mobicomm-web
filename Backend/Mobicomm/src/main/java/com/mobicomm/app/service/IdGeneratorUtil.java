package com.mobicomm.app.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class IdGeneratorUtil {

    private final AtomicInteger purchaseCounter = new AtomicInteger(0);
    private final AtomicInteger paymentCounter = new AtomicInteger(0);
    private final AtomicInteger rechargeCounter = new AtomicInteger(0);
    private final AtomicInteger activePlanCounter = new AtomicInteger(0);

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public String generatePurchaseId() {
        return "PUR-" + getCurrentDate() + "-" + String.format("%04d", purchaseCounter.incrementAndGet());
    }

    public String generatePaymentId() {
        return "PAY-" + getCurrentDate() + "-" + String.format("%04d", paymentCounter.incrementAndGet());
    }

    public String generateRechargeHistoryId() {
        return "RCH-" + getCurrentDate() + "-" + String.format("%04d", rechargeCounter.incrementAndGet());
    }

    public String generateActivePlanId() {
        return "ACT-" + getCurrentDate() + "-" + String.format("%04d", activePlanCounter.incrementAndGet());
    }
}

