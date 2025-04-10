package com.javaweb.controller;

import com.javaweb.dto.ProductDTO;
import com.javaweb.model.ProductsEntity;
import com.javaweb.model.ResponseObject;
import com.javaweb.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/{categorySlug}")
    public ResponseEntity<ResponseObject<List<ProductDTO>>> getProductsByCategorySlug(@PathVariable String categorySlug) {
        return ResponseEntity.ok(productService.findProductsByCategorySlug(categorySlug));
    }

    @GetMapping
    public ResponseEntity<ResponseObject<Page<ProductDTO>>> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        ResponseObject<Page<ProductDTO>> products = productService.findAllProducts(pageable);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject<List<ProductDTO>>> searchProductsByName(
            @RequestParam String keyword) {
        ResponseObject<List<ProductDTO>> response = productService.findProductsByName(keyword);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/by-discount")
    public ResponseEntity<ResponseObject<List<ProductDTO>>> getProductsByDiscount() {
        ResponseObject<List<ProductDTO>> response = productService.findProductByDiscount();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ResponseObject<ProductDTO>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseObject<ProductsEntity>> saveOrUpdate(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.saveOrUpdateProduct(productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> deleteProduct(@RequestParam Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("/filter/price")
    public ResponseEntity<ResponseObject<List<ProductDTO>>> getProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String categorySlug) {
        ResponseObject<List<ProductDTO>> response = productService.findProductByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/sort")
    public ResponseEntity<ResponseObject<List<ProductDTO>>> getProductsSortedBy(
            @RequestParam String sortBy,
            @RequestParam(required = false) String categorySlug){
        ResponseObject<List<ProductDTO>> response = productService.findProductsSortedBy(sortBy);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/new-product")
    public ResponseEntity<ResponseObject<ProductsEntity>> createNewProduct(@RequestBody ProductDTO productDTO) {
        ResponseObject<ProductsEntity> response = productService.createNewProduct(productDTO);
        return new ResponseEntity<>(response, response.getStatus());
    }


}
