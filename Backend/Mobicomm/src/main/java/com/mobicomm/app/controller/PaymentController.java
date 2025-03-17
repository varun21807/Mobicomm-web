//package com.mobicomm.app.controller;
//
//import com.mobicomm.app.model.PaymentRequest;
//import com.mobicomm.app.model.PaymentResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/payment")
//public class PaymentController {
//
//    @Value("${cashfree.clientId}")
//    private String clientId;
//
//    @Value("${cashfree.clientSecret}")
//    private String clientSecret;
//
//    @Value("${cashfree.baseUrl}")
//    private String cashfreeBaseUrl; // "https://sandbox.cashfree.com/pg"
//
//    private final WebClient webClient;
//
//    // Constructor-based injection of WebClient.Builder
//    public PaymentController(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl(cashfreeBaseUrl).build();
//    }
//
//    @PostMapping("/create-order")
//    public Mono<ResponseEntity<PaymentResponse>> createOrder(@RequestBody PaymentRequest paymentRequest) {
//        // Prepare request body
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("order_id", paymentRequest.getOrderId());
//        requestBody.put("order_amount", paymentRequest.getOrderAmount());
//        requestBody.put("order_currency", "INR");
//        requestBody.put("customer_details", paymentRequest.getCustomerDetails());
//
//        // Call Cashfree API using WebClient
//        return webClient.post()
//                .uri("/orders")
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .header("x-client-id", clientId)
//                .header("x-client-secret", clientSecret)
//                .header("x-api-version", "2022-01-01")
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(PaymentResponse.class)
//                .map(ResponseEntity::ok);
//    }
//}
