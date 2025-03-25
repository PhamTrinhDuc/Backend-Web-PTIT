package com.javaweb.controller;


import com.javaweb.dto.ProductDTO;
import com.javaweb.dto.ProductVariantDTO;
import com.javaweb.model.ProductVariantEntity;
import com.javaweb.model.ResponseObject;
import com.javaweb.service.ProductVariantService;
import com.javaweb.service.impl.ProductVariantImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/product_variant")
public class ProductVariantController {

    @Autowired
    private ProductVariantImpl productVariantService;

    @GetMapping
    public ResponseEntity<ResponseObject<Page<ProductVariantDTO>>> getAllProductVariant(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        ResponseObject<Page<ProductVariantDTO>> productVariants = productVariantService.getAllProductVariant(pageable);
        return ResponseEntity.ok(productVariants);
    }
    @GetMapping("/{categorySlug}")
    public ResponseEntity<ResponseObject<List<ProductVariantDTO>>> getProductVariantsByCategorySlug(
            @PathVariable String categorySlug) {
        ResponseObject<List<ProductVariantDTO>> response = productVariantService.getProductVariantsByCategorySlug(categorySlug);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ResponseObject<ProductVariantDTO>> getProductVariantById(@PathVariable Long id) {
//        return ResponseEntity.ok(productVariantService.getProductVariantById(id));
//    }

    @PostMapping
    public ResponseEntity<ResponseObject<ProductVariantEntity>> saveOrUpdateProductVariant(@RequestBody ProductVariantDTO productVariantDTO) {
        return ResponseEntity.ok(productVariantService.saveOrUpdateProductVariant(productVariantDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> deleteProductVariant(@RequestParam Long id) {
        return ResponseEntity.ok(productVariantService.deleteProductVariant(id));
    }
}
