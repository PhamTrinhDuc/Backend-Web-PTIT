//package com.javaweb.api;
//
//import com.javaweb.model.ProductsEntity;
//import com.javaweb.service.CategoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//public class ProductAPI {
//    @Autowired
//    private CategoryService productService;
//
//    @PostMapping("api/production/")
//    public List<ProductsEntity> getBuildingList(@RequestBody ProductsEntity productParams) {
//        return productService.getProductByParams(productParams);
//    }
//}
