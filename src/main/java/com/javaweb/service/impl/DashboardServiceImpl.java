package com.javaweb.service.impl;

import com.javaweb.repository.OrderRepository;
import com.javaweb.repository.ProductRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Map<String, Object> getOverviewStats() {
        Map<String, Object> stats = new HashMap<>();
        
        Double totalRevenue = orderRepository.getTotalRevenue();
        stats.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        stats.put("totalOrders", orderRepository.getTotalOrderCount());
        stats.put("totalProducts", productRepository.count());
        stats.put("totalCustomers", userRepository.count());
        
        return stats;
    }

    @Override
    public Map<Integer, Double> getMonthlyRevenue(int year) {
        List<Object[]> data = orderRepository.getMonthlyRevenue(year);
        Map<Integer, Double> monthlyRevenue = new TreeMap<>();
        
        // Khởi tạo 12 tháng với giá trị 0
        for (int i = 1; i <= 12; i++) {
            monthlyRevenue.put(i, 0.0);
        }
        
        for (Object[] row : data) {
            Integer month = (Integer) row[0];
            Double total = (Double) row[1];
            monthlyRevenue.put(month, total);
        }
        
        return monthlyRevenue;
    }
}
