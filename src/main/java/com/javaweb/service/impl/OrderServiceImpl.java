package com.javaweb.service.impl;

import com.javaweb.dto.OrderDTO;
import com.javaweb.dto.OrderDetailDTO;
import com.javaweb.model.*;
import com.javaweb.repository.*;
import com.javaweb.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ModelMapper modelMapper; // Inject ModelMapper
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    // tạo đơn hàng mới
    @Transactional
    public OrderEntity createOrder(OrderDTO orderDTO) {
        // 1. Lấy thông tin người dùng
        UserEntity user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + orderDTO.getUserId()));

        // 2. Tạo đơn hàng mới
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalAmount(0.0);
        order.setPaymentMethod(orderDTO.getPaymentMethod()); // Thêm payment method từ DTO

        // 3. Kiểm tra và cập nhật số lượng tồn kho
        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderDetailDTO itemDTO : orderDTO.getItems()) {
            ProductsEntity product = productRepository
                    .findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDTO.getProductId()));

            // Kiểm tra số lượng trong kho
            if (product.getQuantityStock() < itemDTO.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + itemDTO.getProductId() +
                        ". Available: " + product.getQuantityStock() + ", Requested: " + itemDTO.getQuantity());
            }

            // Giảm số lượng trong kho
            product.setQuantityStock(product.getQuantityStock() - itemDTO.getQuantity());
            productRepository.save(product); // Lưu lại sản phẩm đã thay đổi số lượng

            // Tạo chi tiết đơn hàng
            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setOrder(order);
            orderDetail.setProducts(product); // Gán ProductsEntity vào orderDetail
            orderDetail.setQuantity(itemDTO.getQuantity());
            orderDetail.setUnitPrice(product.getPrice());
            orderDetail.setDiscount(itemDTO.getDiscount()); // Thêm discount từ DTO nếu có
            orderDetails.add(orderDetail);

            // Cộng dồn tổng số tiền
            totalAmount += (product.getPrice() * itemDTO.getQuantity()) * (1 - itemDTO.getDiscount() / 100); // Tính discount nếu có
        }
        // 4. Cập nhật tổng số tiền cho đơn hàng
        order.setTotalAmount(totalAmount);
        // 5. Gán danh sách chi tiết đơn hàng vào đơn hàng
        order.setOrderDetails(orderDetails);
        // 6. Lưu đơn hàng
        OrderEntity savedOrder = orderRepository.save(order);
        // 7. Cập nhật trạng thái đơn hàng
        return orderRepository.save(savedOrder);
    }

    // Thêm phương thức cancelOrder
    @Transactional
    public OrderEntity cancelOrder(Long orderId) {
        // 1. Lấy thông tin đơn hàng
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // 2. Kiểm tra trạng thái đơn hàng
        if (order.getStatus().equals("SHIPPED") || order.getStatus().equals("DELIVERED") ) { // đơn đang được giao mới được hủy
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

    @Override
    public List<OrderDTO> getAllOrders(Long id) {
        List<OrderEntity> orders = orderRepository.findByUser_Id(id);

        List<OrderDTO> orderDTO =  orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
        return orderDTO;
    }

    @Transactional
    public OrderDTO updateOrder(Long id, String status ){
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        if (orderEntity.isEmpty()) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        OrderEntity order = orderEntity.get();
        order.setStatus(status); // Cập nhật status mới
        orderRepository.save(order); // Lưu lại thay đổi
        return modelMapper.map(order, OrderDTO.class); // Convert sang DTO nếu cần trả về
    }
}