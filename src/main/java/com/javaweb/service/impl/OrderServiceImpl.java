package com.javaweb.service.impl;

import com.javaweb.dto.OrderDTO;
import com.javaweb.dto.OrderDetailDTO;
import com.javaweb.model.*;
import com.javaweb.repository.*;
import com.javaweb.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public OrderEntity createOrder(OrderDTO orderDTO){
        // 1. Lấy thông tin người dùng
        UserEntity user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + orderDTO.getUserId()));

        // 2. Tạo đơn hàng mới
        OrderEntity order = new OrderEntity();
        order.setUserEntity(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalAmount(0.0);
        order.setPaymentMethod(orderDTO.getPaymentMethod());

        // 3. Kiểm tra và cập nhật số lượng tồn kho
        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        double totalAmount = 0.0;

        for(OrderDetailDTO itemDTO: orderDTO.getItems()){
            ProductVariantEntity productVariant = productVariantRepository
                    .findById(itemDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Product variant not found: " + itemDTO.getId()));

            // kiểm tra số lượng trong kho
            if (productVariant.getQuantityStock() < itemDTO.getQuantity()) {
                throw new RuntimeException("Not enough stock for product variant: " + itemDTO.getId() +
                        ". Available: " + productVariant.getQuantityStock() + ", Requested: " + itemDTO.getQuantity());
            }

            // Giảm số lượng trong kho
            productVariant.setQuantityStock(productVariant.getQuantityStock() - itemDTO.getQuantity());
            productVariantRepository.save(productVariant);

            // Tạo chi tiết đơn hàng
            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setOrder(order);
            orderDetail.setProductVariant(productVariant);
            orderDetail.setQuantity(itemDTO.getQuantity());
            orderDetail.setUnitPrice(productVariant.getPrice());
            orderDetails.add(orderDetail);

            // Cộng dồn tổng số tiền
            totalAmount += (productVariant.getPrice() * itemDTO.getQuantity());
        }
        // 4. Cập nhật tổng số tiền cho đơn hàng
        order.setTotalAmount(totalAmount);

        // 5. Gán danh sách chi tiết đơn hàng vào đơn hàng
        order.setOrderDetails(orderDetails);

        // 6. Lưu đơn hàng
        OrderEntity savedOrder = orderRepository.save(order);

        // 7. Cập nhật trạng thái đơn hàng
        savedOrder.setStatus("COMPLETED");
        return orderRepository.save(savedOrder);
    }

    // Thêm phương thức cancelOrder
    @Transactional
    public OrderEntity cancelOrder(Long orderId) {
        // 1. Lấy thông tin đơn hàng
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // 2. Kiểm tra trạng thái đơn hàng
        if (!order.getStatus().equals("PENDING")) { // đơn đang được giao mới được hủy
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }

        // 3. Tăng lại số lượng tồn kho cho từng sản phẩm trong đơn hàng
        for (OrderDetailEntity orderDetail : order.getOrderDetails()) {
            ProductVariantEntity productVariant = orderDetail.getProductVariant();

            // Sử dụng khóa bi quan để tránh race condition
            ProductVariantEntity finalProductVariant = productVariant;

            productVariant = productVariantRepository
                    .findById(productVariant.getId())
                    .orElseThrow(() -> new RuntimeException("Product variant not found: " + finalProductVariant.getId()));

            // Tăng lại số lượng
            productVariant.setQuantityStock(productVariant.getQuantityStock() + orderDetail.getQuantity());
            productVariantRepository.save(productVariant);
        }

        // 4. Xử lý thanh toán (nếu có)
        PaymentEntity payment = paymentRepository.findByOrderId(orderId).orElse(null);
        if (payment != null) {
            // Cập nhật trạng thái thanh toán thành REFUNDED
            payment.setStatus("REFUNDED");
            paymentRepository.save(payment);
        }
        order.setStatus("CANCELLED");
        return orderRepository.save(order);
    }
}
