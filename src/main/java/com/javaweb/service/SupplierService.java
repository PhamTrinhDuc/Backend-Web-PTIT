package com.javaweb.service;

import com.javaweb.dto.SupplierDTO;
import com.javaweb.model.ResponseObject;
import com.javaweb.model.SupplierEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface SupplierService {
    ResponseObject<List<SupplierDTO>> getAllSupplier();
    ResponseObject<SupplierDTO> getBySupplierName(String name);
    ResponseObject<SupplierDTO> addSupplier(SupplierDTO supplierDTO);
    ResponseObject<SupplierDTO> updateSupplier(SupplierDTO supplierDTO);
    ResponseObject<String> deleteSupplier(Long id);
}
