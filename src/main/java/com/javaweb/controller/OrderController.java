package com.javaweb.controller;


import com.javaweb.dto.OrderDTO;
import com.javaweb.model.OrderEntity;
import com.javaweb.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderRequestDTO) {
        try {
            orderService.createOrder(orderRequestDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Collections.singletonMap("message", "Order created successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Unexpected error: " + e.getMessage()));
        }
    }


    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        try {
            OrderEntity cancelledOrder = orderService.cancelOrder(id);
            return ResponseEntity.ok(cancelledOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
