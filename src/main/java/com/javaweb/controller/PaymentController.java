package com.javaweb.controller;

import com.javaweb.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-payos")
    public ResponseEntity<?> createPayOSPayment(@RequestBody Map<String, Object> request) {
        try {
            Long orderId = Long.valueOf(request.get("orderId").toString());
            // Amount and description are now handled inside service based on orderId
            CreatePaymentLinkResponse response = paymentService.createPayOSPayment(orderId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/confirm-payment")
    public ResponseEntity<?> confirmPayment(@RequestParam Long orderCode, @RequestParam String status) {
        try {
            String result = paymentService.confirmPayment(orderCode, status);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/payos-webhook")
    public ResponseEntity<?> payosWebhook(@RequestBody Map<String, Object> webhookData) {
        try {
            // webhookData example from PayOS: { "data": { "orderCode": 123, "status": "PAID" }, ... }
            if (webhookData.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) webhookData.get("data");
                Long orderCode = Long.valueOf(data.get("orderCode").toString());
                String status = data.get("status").toString();

                paymentService.confirmPayment(orderCode, status);
            }
            return ResponseEntity.ok(Map.of("message", "Webhook processed success"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
