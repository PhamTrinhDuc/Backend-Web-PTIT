package com.javaweb.service;

import com.javaweb.model.OrderEntity;
import com.javaweb.model.OrderDetailEntity;
import com.javaweb.model.Transaction;
import com.javaweb.repository.OrderRepository;
import com.javaweb.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;
//import vn.payos.type.CheckoutResponseData;
//import vn.payos.type.ItemData;
//import vn.payos.type.PaymentData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PayOS payOS;

    @Value("${payos.return-url}")
    private String returnUrl;

    @Value("${payos.cancel-url}")
    private String cancelUrl;

    @Transactional
    public CreatePaymentLinkResponse createPayOSPayment(Long orderId) throws Exception {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Create ItemData list from OrderDetails
        List<PaymentLinkItem> items = new ArrayList<>();
        if (order.getOrderDetails() != null) {
            for (OrderDetailEntity detail : order.getOrderDetails()) {
                items.add(PaymentLinkItem.builder()
                        .name(detail.getProducts().getName())
                        .quantity(detail.getQuantity())
                        .price((long) detail.getUnitPrice())
                        .build());
            }
        }

        // Amount must be integer (VND)
        long amount = (long) order.getTotalAmount();
        String description = "Thanh toan don hang #" + orderId;

        // Prepare payment data
        CreatePaymentLinkRequest paymentData =
                CreatePaymentLinkRequest.builder()
                        .orderCode(orderId)
                        .amount(amount)
                        .description(description)
                        .returnUrl(returnUrl)
                        .cancelUrl(cancelUrl)
                        .items(items)
                        .build();

        CreatePaymentLinkResponse data =
                payOS.paymentRequests().create(paymentData);

        // Save transaction to database
        Transaction transaction = new Transaction();
        transaction.setOrderCode(orderId);
        transaction.setPaymentId(data.getPaymentLinkId());
        transaction.setStatus("PENDING");
        transaction.setAmount(String.valueOf(amount));
        transaction.setCurrency("VND");
        transaction.setDescription(description);
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        return data;
    }

    @Transactional
    public String confirmPayment(Long orderCode, String status) {
        Transaction transaction = transactionRepository.findByOrderCode(orderCode);
        if (transaction != null) {
            transaction.setStatus(status);
            transactionRepository.save(transaction);

            if ("PAID".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status)) {
                OrderEntity order = orderRepository.findById(orderCode).orElse(null);
                if (order != null) {
                    order.setStatus("PAID");
                    orderRepository.save(order);
                }
            }
        }
        return "Payment processed for order: " + orderCode + " with status: " + status;
    }
}

