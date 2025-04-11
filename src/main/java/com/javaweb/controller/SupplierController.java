package com.javaweb.controller;

import com.javaweb.dto.SupplierDTO;
import com.javaweb.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // Lấy tất cả nhà cung cấp
    @GetMapping
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers() {
        List<SupplierDTO> suppliers = supplierService.getAllSupplier();
        return ResponseEntity.ok(suppliers);
    }

    // Tìm nhà cung cấp theo tên
    @GetMapping("/search")
    public ResponseEntity<SupplierDTO> getSupplierByName(@RequestParam String name) {
        SupplierDTO supplier = supplierService.getBySupplierName(name);
        return ResponseEntity.ok(supplier);
    }
}
