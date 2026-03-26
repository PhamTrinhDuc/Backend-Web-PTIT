package com.javaweb.service;

import java.util.Map;

public interface DashboardService {
    Map<String, Object> getOverviewStats();
    Map<Integer, Double> getMonthlyRevenue(int year);
}
