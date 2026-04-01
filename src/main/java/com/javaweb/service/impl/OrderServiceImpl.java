package com.javaweb.service.impl;

import com.javaweb.dto.OrderDTO;
import com.javaweb.dto.OrderDetailDTO;
import com.javaweb.model.*;
import com.javaweb.repository.*;
import com.javaweb.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
    public OrderDTO createOrder(OrderDTO orderDTO) {
        // ... (Giữ nguyên logic tạo đơn hàng ở trên)
        // 1. Lấy thông tin người dùng
        UserEntity user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + orderDTO.getUserId()));

        // 2. Tạo đơn hàng mới
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalAmount(0.0);
        order.setPaymentMethod(orderDTO.getPaymentMethod()); 
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setContactPhone(orderDTO.getContactPhone());
        order.setContactName(orderDTO.getContactName());

        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderDetailDTO itemDTO : orderDTO.getItems()) {
            ProductsEntity product = productRepository
                    .findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDTO.getProductId()));
            
            if (product.getQuantityStock() < itemDTO.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + itemDTO.getProductId());
            }

            product.setQuantityStock(product.getQuantityStock() - itemDTO.getQuantity());
            productRepository.save(product);

            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setOrder(order);
            orderDetail.setProducts(product);
            orderDetail.setQuantity(itemDTO.getQuantity());
            orderDetail.setUnitPrice(product.getPrice());
            orderDetail.setDiscount(itemDTO.getDiscount());
            orderDetails.add(orderDetail);

            totalAmount += (product.getPrice() * itemDTO.getQuantity()) * (1 - itemDTO.getDiscount() / 100);
        }
        order.setTotalAmount(totalAmount);
        order.setOrderDetails(orderDetails);
        
        OrderEntity savedOrder = orderRepository.save(order);
        
        // Trả về DTO thay vì Entity để tránh lỗi Jackson Serialization
        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    @Transactional
    public OrderDTO cancelOrder(Long orderId) {
        // 1. Lấy thông tin đơn hàng
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // 2. Kiểm tra trạng thái đơn hàng
        if (order.getStatus().equals("SHIPPED") || order.getStatus().equals("DELIVERED") || order.getStatus().equals("CANCELLED")) {
            // Nếu đơn hàng đang giao hoặc đã giao, không thể hủy
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }

        // 3. Tăng lại số lượng tồn kho cho từng sản phẩm trong đơn hàng
        for (OrderDetailEntity orderDetail : order.getOrderDetails()) {
            ProductsEntity product = orderDetail.getProducts();

            ProductsEntity productCp = product;

            // Lấy lại sản phẩm với khóa bi quan để tránh race condition
            product = productRepository.findById(product.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productCp.getId()));

            // Tăng lại số lượng tồn kho của sản phẩm
            product.setQuantityStock(product.getQuantityStock() + orderDetail.getQuantity());
            productRepository.save(product);
        }

        // Cập nhật trạng thái đơn hàng thành "CANCELLED"
        order.setStatus("CANCELLED");

        // Lưu lại đơn hàng đã hủy
        OrderEntity cancelledOrder = orderRepository.save(order);

        // Chuyển đổi OrderEntity thành OrderDTO sử dụng modelMapper
        OrderDTO cancelledOrderDTO = modelMapper.map(cancelledOrder, OrderDTO.class);

        // Trả về ResponseObject với dữ liệu OrderDTO
        return cancelledOrderDTO;
    }

    @Override
    public ResponseObject<Page<OrderDTO>> getAllOrders(Pageable pageable) {
        Page<OrderEntity> orders = orderRepository.findAll(pageable);

        Page<OrderDTO> orderDTO =  orders.map(order -> modelMapper.map(order, OrderDTO.class));
        return ResponseObject.success(orderDTO);
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

    @Override
    public ResponseObject<Page<OrderDTO>> getAllOrdersByUserId(Pageable pageable, Long userId) {
        // Kiểm tra userId hợp lệ
        if (userId == null || userId <= 0) {
            return ResponseObject.error("Invalid user ID", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Truy vấn đơn hàng theo userId
        Page<OrderEntity> orders = orderRepository.findByUser_Id(pageable, userId);

        // Chuyển đổi Page<OrderEntity> sang Page<OrderDTO>
        Page<OrderDTO> orderDTOs = orders.map(order -> modelMapper.map(order, OrderDTO.class));

        // Trả về kết quả
        return ResponseObject.success(orderDTOs);
    }
}