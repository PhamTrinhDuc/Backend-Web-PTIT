package com.javaweb.controller;

import com.javaweb.dto.SupplierDTO;
import com.javaweb.model.ResponseObject;
import com.javaweb.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<ResponseObject<List<SupplierDTO>>> getAllSuppliers() {
        ResponseObject<List<SupplierDTO>> suppliers = supplierService.getAllSupplier();
        return ResponseEntity.ok(suppliers);
    }

    // Tìm nhà cung cấp theo tên
    @GetMapping("/search")
    public ResponseEntity<ResponseObject<SupplierDTO>> getSupplierByName(@RequestParam String name) {
        ResponseObject<SupplierDTO> supplier = supplierService.getBySupplierName(name);
        return ResponseEntity.ok(supplier);
    }

    @PostMapping
    public ResponseEntity<ResponseObject<SupplierDTO>> addSupplier(@RequestBody SupplierDTO supplierDTO) {
        ResponseObject<SupplierDTO> createdSupplier = supplierService.addSupplier(supplierDTO);
        return ResponseEntity.ok(createdSupplier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<SupplierDTO>> updateSupplier(@PathVariable Long id,
                                                                      @RequestBody SupplierDTO supplierDTO) {
        supplierDTO.setId(id);
        supplierDTO.setActive(true);
        ResponseObject<SupplierDTO> updatedSupplier = supplierService.updateSupplier(supplierDTO);
        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<String>> deleteSupplier(@PathVariable Long id) {
        ResponseObject<String> response = supplierService.deleteSupplier(id);
        return ResponseEntity.ok(response);
    }
}
