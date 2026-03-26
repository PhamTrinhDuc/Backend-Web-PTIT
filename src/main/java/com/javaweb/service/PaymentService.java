package com.javaweb.service;

import com.javaweb.config.PaymentConfig;
import com.javaweb.dto.PaymentRequest;
import com.javaweb.model.Transaction;
import com.javaweb.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentConfig paymentConfig;

    public String createPayment(PaymentRequest paymentRequest) {
        // Save transaction to database
        Transaction transaction = new Transaction();
        transaction.setPaymentId("12345"); // Replace with actual payment ID from gateway
        transaction.setStatus("PENDING");
        transaction.setAmount(paymentRequest.getAmount());
        transaction.setCurrency(paymentRequest.getCurrency());
        transaction.setDescription(paymentRequest.getDescription());
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Logic to interact with third-party payment gateway to create a payment
        // Example: Call the payment gateway API with paymentRequest details
        // Use paymentConfig.getApiKey(), paymentConfig.getSecretKey(), paymentConfig.getBaseUrl()

        return "Payment created successfully with ID: " + transaction.getPaymentId();
    }

    public String confirmPayment(String paymentId, String token) {
        // Retrieve transaction from database
        Transaction transaction = transactionRepository.findByPaymentId(paymentId);
        if (transaction == null) {
            throw new RuntimeException("Transaction not found");
        }

        // Logic to confirm the payment with the third-party payment gateway
        // Example: Verify the paymentId and token with the payment gateway
        transaction.setStatus("CONFIRMED");
        transactionRepository.save(transaction);

        return "Payment confirmed successfully for ID: " + paymentId;
    }
}
