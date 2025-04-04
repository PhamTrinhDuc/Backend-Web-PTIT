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
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public OrderEntity createOrder(OrderDTO orderDTO){
        // 1. Lấy thông tin người dùng
        UserEntity user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + orderDTO.getUserId()));

        // 2. Tạo đơn hàng mới
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalAmount(0.0);

        // 3. Kiểm tra và cập nhật số lượng tồn kho
        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderDetailDTO itemDTO : orderDTO.getItems()) {
            ProductsEntity product = productRepository
                    .findById(itemDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Product variant not found: " + itemDTO.getId()));

            // Kiểm tra số lượng trong kho
            if (product.getQuantityStock() < itemDTO.getQuantity()) {
                throw new RuntimeException("Not enough stock for product variant: " + itemDTO.getId() +
                        ". Available: " + product.getQuantityStock() + ", Requested: " + itemDTO.getQuantity());
            }

            // Giảm số lượng trong kho
            product.setQuantityStock(product.getQuantityStock() - itemDTO.getQuantity());

            // Lưu lại sản phẩm đã thay đổi số lượng
            productRepository.save(product);  // Đảm bảo gọi từ productRepository đã được inject

            // Tạo chi tiết đơn hàng
            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setOrder(order);
            orderDetail.setQuantity(itemDTO.getQuantity());
            orderDetail.setUnitPrice(product.getPrice());
            orderDetails.add(orderDetail);

            // Cộng dồn tổng số tiền
            totalAmount += (product.getPrice() * itemDTO.getQuantity());
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
            ProductsEntity productVariant = orderDetail.getProducts();

            // Sử dụng khóa bi quan để tránh race condition
            ProductsEntity finalProductVariant = productVariant;

            productVariant = productRepository
                    .findById(productVariant.getId())
                    .orElseThrow(() -> new RuntimeException("Product variant not found: " + finalProductVariant.getId()));

            // Tăng lại số lượng
            productVariant.setQuantityStock(productVariant.getQuantityStock() + orderDetail.getQuantity());
            productRepository.save(productVariant);
        }

        order.setStatus("CANCELLED");
        return orderRepository.save(order);
    }
}
