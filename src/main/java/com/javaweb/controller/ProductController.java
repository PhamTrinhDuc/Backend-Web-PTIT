package com.javaweb.controller;


import com.javaweb.dto.ProductDTO;
import com.javaweb.model.ProductsEntity;
import com.javaweb.model.ResponseObject;
import com.javaweb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<ResponseObject<Page<ProductDTO>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        ResponseObject<Page<ProductDTO>> products = productService.findAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<ProductDTO>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseObject<ProductsEntity>> saveOrUpdateProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.saveOrUpdateProduct(productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> deleteProduct(@RequestParam Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
