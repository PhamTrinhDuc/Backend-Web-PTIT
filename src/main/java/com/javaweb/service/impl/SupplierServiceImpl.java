package com.javaweb.service.impl;

import com.javaweb.dto.SupplierDTO;
import com.javaweb.model.SupplierEntity;
import com.javaweb.repository.SupplierRespository;
import com.javaweb.service.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRespository supplierRespository;
    private final ModelMapper modelMapper;

    @Autowired
    public SupplierServiceImpl(SupplierRespository supplierRespository, ModelMapper modelMapper) {
        this.supplierRespository = supplierRespository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SupplierDTO> getAllSupplier() {
        try {
            List<SupplierEntity> suppliers = supplierRespository.findAll();
            return suppliers.stream()
                    .map(supplier -> modelMapper.map(supplier, SupplierDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách nhà cung cấp: " + e.getMessage(), e);
        }
    }

    @Override
    public SupplierDTO getBySupplierName(String name) {
        try {
            SupplierEntity supplier = supplierRespository.findByName(name)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp tên: " + name));
            return modelMapper.map(supplier, SupplierDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy nhà cung cấp theo tên: " + e.getMessage(), e);
        }
    }
}
