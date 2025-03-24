package com.javaweb.service;

import com.javaweb.dto.OrderDTO;
import com.javaweb.model.OrderEntity;

public interface OrderService {
    OrderEntity createOrder(OrderDTO orderDTO);
}
