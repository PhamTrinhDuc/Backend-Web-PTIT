package com.javaweb.service.impl;

import com.javaweb.dto.SupplierDTO;
import com.javaweb.model.ResponseObject;
import com.javaweb.model.SupplierEntity;
import com.javaweb.repository.SupplierRespository;
import com.javaweb.service.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRespository supplierRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SupplierServiceImpl(SupplierRespository supplierRespository, ModelMapper modelMapper) {
        this.supplierRepository = supplierRespository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseObject<List<SupplierDTO>> getAllSupplier() {
        try {
            List<SupplierEntity> suppliers = supplierRepository.findAll();
            List<SupplierDTO> supplierDTOs = suppliers.stream()
                    .map(supplier -> modelMapper.map(supplier, SupplierDTO.class))
                    .collect(Collectors.toList());
            return ResponseObject.success(supplierDTOs);
        } catch (Exception e) {
            return ResponseObject.error("Lỗi khi lấy danh sách nhà cung cấp: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseObject<SupplierDTO> getBySupplierName(String name) {
        try {
            SupplierEntity supplier = supplierRepository.findByName(name)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp tên: " + name));
            SupplierDTO supplierDTO = modelMapper.map(supplier, SupplierDTO.class);
            return ResponseObject.success(supplierDTO);
        } catch (RuntimeException e) {
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseObject.error("Lỗi khi lấy nhà cung cấp theo tên: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseObject<SupplierDTO> addSupplier(SupplierDTO supplierDTO) {
        try {
            supplierDTO.setActive(true);
            SupplierEntity supplierEntity = modelMapper.map(supplierDTO, SupplierEntity.class);
            SupplierEntity savedSupplier = supplierRepository.save(supplierEntity);
            SupplierDTO savedSupplierDTO = modelMapper.map(savedSupplier, SupplierDTO.class);
            return ResponseObject.success(savedSupplierDTO);
        } catch (Exception e) {
            return ResponseObject.error("Failed to add supplier: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseObject<SupplierDTO> updateSupplier(SupplierDTO supplierDTO) {
        try {
            SupplierEntity existingSupplier = supplierRepository.findById(supplierDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + supplierDTO.getId()));
            modelMapper.map(supplierDTO, existingSupplier);
            SupplierEntity updatedSupplier = supplierRepository.save(existingSupplier);
            SupplierDTO updatedSupplierDTO = modelMapper.map(updatedSupplier, SupplierDTO.class);
            return ResponseObject.success(updatedSupplierDTO);
        } catch (RuntimeException e) {
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseObject.error("Failed to update supplier: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseObject<String> deleteSupplier(Long id) {
        try {
            SupplierEntity supplier = supplierRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
            supplierRepository.delete(supplier);
            return ResponseObject.success("Supplier deleted successfully");
        } catch (RuntimeException e) {
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseObject.error("Failed to delete supplier: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}