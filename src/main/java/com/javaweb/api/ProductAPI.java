package com.javaweb.api;

import com.javaweb.model.ProductDTO;
import com.javaweb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductAPI {
    @Autowired
    private  ProductService productService;

    @PostMapping("api/production/")
    public List<ProductDTO> getBuildingList(@RequestBody ProductDTO productParams) {
        return productService.getProductByParams(productParams);
    }
}
