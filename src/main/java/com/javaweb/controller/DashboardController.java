package com.javaweb.controller;

import com.javaweb.model.ResponseObject;
import com.javaweb.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/overview")
    public ResponseEntity<ResponseObject<Map<String, Object>>> getOverview() {
        return ResponseEntity.ok(ResponseObject.success(dashboardService.getOverviewStats()));
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity<ResponseObject<Map<Integer, Double>>> getMonthlyRevenue(@RequestParam(defaultValue = "2025") int year) {
        return ResponseEntity.ok(ResponseObject.success(dashboardService.getMonthlyRevenue(year)));
    }
}
