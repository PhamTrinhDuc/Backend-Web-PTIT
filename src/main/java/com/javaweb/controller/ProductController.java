package com.javaweb.controller;

import com.javaweb.dto.AddProductRequestDTO;
import com.javaweb.dto.ProductDTO;
import com.javaweb.dto.UpdateProductRequestDTO;
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
    public ResponseEntity<ResponseObject<Page<ProductDTO>>> findProductsByCategorySlug(
            @PathVariable String categorySlug,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size);
        ResponseObject<Page<ProductDTO>> products = productService.findProductsByCategorySlug(
                categorySlug, page, size,
                minPrice, maxPrice, sortBy);

        return ResponseEntity.ok(products);
    }

    @GetMapping
    public ResponseEntity<ResponseObject<Page<ProductDTO>>> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy
    ) {
        ResponseObject<Page<ProductDTO>> products = productService.findAllProducts(minPrice, maxPrice, page, size, sortBy);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject<Page<ProductDTO>>> searchProductsByName(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size
    ) {
        ResponseObject<Page<ProductDTO>> result;

        if (keyword != null && !keyword.isEmpty()) {
            result = productService.findProductsByName(keyword, page, size);
        } else {
            Pageable pageable = PageRequest.of(page, size);
            result = productService.findAllProducts(null, null, page, size, null); // fallback nếu không có keyword
        }
        return ResponseEntity.ok(result);
    }


    @GetMapping("/filter/by-discount")
    public ResponseEntity<ResponseObject<Page<ProductDTO>>> getProductsByDiscount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        ResponseObject<Page<ProductDTO>> products = productService.findProductByDiscount(page, size);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<ResponseObject<ProductDTO>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseObject<ProductsEntity>> saveOrUpdate(@RequestBody UpdateProductRequestDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("/filter/price")
    public ResponseEntity<ResponseObject<Page<ProductDTO>>> getProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        ResponseObject<Page<ProductDTO>> response = productService.findProductByPriceRange(minPrice, maxPrice, page, size);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/filter/sort")
    public ResponseEntity<ResponseObject<List<ProductDTO>>> getProductsSortedBy(
            @RequestParam String sortBy,
            @RequestParam(required = false) String categorySlug){
        ResponseObject<List<ProductDTO>> response = productService.findProductsSortedBy(sortBy);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/new-product")
    public ResponseEntity<ResponseObject<ProductsEntity>> createNewProduct(@RequestBody AddProductRequestDTO productDTO) {
        ResponseObject<ProductsEntity> response = productService.createNewProduct(productDTO);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
