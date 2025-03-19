package com.javaweb.service.impl;

import com.javaweb.model.ProductsEntity;
import com.javaweb.repository.ProductRepository;
import com.javaweb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductsEntity> findAllProducts() {
       return  productRepository.findAll();
    }
}
