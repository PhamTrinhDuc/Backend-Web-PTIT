package com.javaweb.service;

import com.javaweb.dto.OrderDTO;
import com.javaweb.model.OrderEntity;

import java.util.List;

public interface OrderService {
    OrderEntity createOrder(OrderDTO orderDTO);
    List<OrderDTO> getAllOrders(Long id);
    OrderDTO updateOrder(Long id, String status);
}
