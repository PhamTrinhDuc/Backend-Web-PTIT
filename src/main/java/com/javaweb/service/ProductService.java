package com.javaweb.service;

import com.javaweb.model.ProductsEntity;
import com.javaweb.dto.ProductDTO;
import com.javaweb.model.ResponseObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProductService {
    ResponseObject<Page<ProductDTO>> findAllProducts (Pageable pageable);
    ResponseObject<ProductDTO> findProductById (Long id);
    ResponseObject<ProductsEntity> saveOrUpdateProduct (ProductDTO productDTO);
    ResponseObject<Void> deleteProduct (Long id);
}
