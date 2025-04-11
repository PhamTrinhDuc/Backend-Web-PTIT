package com.javaweb.service;

import com.javaweb.dto.SupplierDTO;

import java.util.List;

public interface SupplierService {
    List<SupplierDTO> getAllSupplier();
    SupplierDTO getBySupplierName(String name);
}
