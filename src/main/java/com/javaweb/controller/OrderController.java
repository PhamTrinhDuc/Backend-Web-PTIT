package com.javaweb.controller;


import com.javaweb.dto.OrderDTO;
import com.javaweb.dto.ProductDTO;
import com.javaweb.model.OrderEntity;
import com.javaweb.model.ResponseObject;
import com.javaweb.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderRequestDTO) {
        try {
            OrderDTO order = orderService.createOrder(orderRequestDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(order);
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
    public ResponseEntity<ResponseObject<OrderDTO>> cancelOrder(@PathVariable Long id) {
        try {
            // Gọi service để hủy đơn hàng
            OrderDTO cancelledOrder = orderService.cancelOrder(id);

            // Trả về ResponseObject chứa OrderDTO đã hủy
            return ResponseEntity.ok(ResponseObject.success(cancelledOrder));
        } catch (RuntimeException e) {
            // Nếu có lỗi trong quá trình hủy, trả về thông báo lỗi
            return ResponseEntity.badRequest().body(ResponseObject.error(e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }


    @GetMapping
    public ResponseEntity<ResponseObject<Page<OrderDTO>>> getAllOrders(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        ResponseObject<Page<OrderDTO>> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        try {
            OrderDTO updatedOrder = orderService.updateOrder(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating order status: " + e.getMessage());
        }
    }

    @GetMapping("/by-id/{user_id}")
    public ResponseEntity<ResponseObject<Page<OrderDTO>>> getAllOrdersByUserId(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @PathVariable Long user_id) {
        Pageable pageable = PageRequest.of(page, size);
        ResponseObject<Page<OrderDTO>> orders = orderService.getAllOrdersByUserId(pageable, user_id);
        return ResponseEntity.ok(orders);
    }
}
