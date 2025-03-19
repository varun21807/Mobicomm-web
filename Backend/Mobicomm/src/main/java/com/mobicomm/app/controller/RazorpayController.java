package com.mobicomm.app.controller;
  // ✅ Correct package

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")

public class CashfreeController {

    private final WebClient webClient;

    @Value("${cashfree.app-id}")    // ✅ Load from application.properties
    private String appId;

    @Value("${cashfree.secret-key}") // ✅ Load from application.properties
    private String secretKey;

    private static final String CASHFREE_SANDBOX_URL = "https://sandbox.cashfree.com/pg/orders";

    public CashfreeController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(CASHFREE_SANDBOX_URL).build();
    }

    @PostMapping("/create-order")
    public Mono<ResponseEntity<Map>> createOrder(@RequestBody Map<String, Object> paymentRequest) {
        return webClient.post()
                .uri("")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("x-client-id", appId)
                .header("x-client-secret", secretKey)
                .header("x-api-version", "2022-09-01")
                .bodyValue(paymentRequest)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> ResponseEntity.ok().body(response))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(Map.of("error", e.getMessage()))));
    }
}
