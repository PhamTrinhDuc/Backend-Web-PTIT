package com.javaweb.service;

import com.javaweb.model.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getProductByParams(ProductDTO getProductByParams);
}
