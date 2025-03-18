package com.javaweb.service.impl;

import com.javaweb.model.ProductDTO;
import com.javaweb.repository.ProductRepository;
import com.javaweb.repository.entity.ProductEntity;
import com.javaweb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> getProductByParams(ProductDTO productParams){
        List<ProductEntity> productEnties = productRepository.getProductByParams(productParams);
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(ProductEntity productEntity : productEnties){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductName(productEntity.getProductName());
            productDTO.setPrice(productEntity.getPrice());
            productDTO.setQuantityStock(productEntity.getQuantityStock());
            productDTO.setDescription(productEntity.getDescription());
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }
}
