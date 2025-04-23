package com.javaweb.service;

import com.javaweb.dto.OrderDTO;
import com.javaweb.dto.ProductDTO;
import com.javaweb.model.OrderEntity;
import com.javaweb.model.ResponseObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderEntity createOrder(OrderDTO orderDTO);
    ResponseObject<Page<OrderDTO>> getAllOrders(Long id, Pageable pageable);
    OrderDTO updateOrder(Long id, String status);
}
