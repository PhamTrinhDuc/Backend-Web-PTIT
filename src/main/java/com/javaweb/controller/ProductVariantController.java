package com.javaweb.controller;

import com.javaweb.dto.ProductVariantDTO;
import com.javaweb.model.ProductVariantEntity;
import com.javaweb.model.ResponseObject;
import com.javaweb.service.impl.ProductVariantImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/product_variant")
public class ProductVariantController {

    @Autowired
    private ProductVariantImpl productVariantService;

    @GetMapping
    public ResponseEntity<ResponseObject<List<ProductVariantDTO>>> getAllProductVariant(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "12") int size
    ) {
//        Pageable pageable = PageRequest.of(page, size);
        ResponseObject<List<ProductVariantDTO>> productVariants = productVariantService.getAllProductVariant();
        return ResponseEntity.ok(productVariants);
    }

    @GetMapping("/by-discount")
    public ResponseEntity<ResponseObject<List<ProductVariantDTO>>> getProductVariantsByDiscount() {
        ResponseObject<List<ProductVariantDTO>> response = productVariantService.getProductVariantByDiscount();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/{categorySlug}")
    public ResponseEntity<ResponseObject<List<ProductVariantDTO>>> getProductVariantsByCategorySlug(
            @PathVariable String categorySlug) {
        ResponseObject<List<ProductVariantDTO>> response = productVariantService.getProductVariantsByCategorySlug(categorySlug);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ResponseObject<ProductVariantDTO>> getProductVariantById(@PathVariable Long id) {
        return ResponseEntity.ok(productVariantService.getProductVariantById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseObject<ProductVariantEntity>> saveOrUpdateProductVariant(@RequestBody ProductVariantDTO productVariantDTO) {
        return ResponseEntity.ok(productVariantService.saveOrUpdateProductVariant(productVariantDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> deleteProductVariant(@RequestParam Long id) {
        return ResponseEntity.ok(productVariantService.deleteProductVariant(id));
    }

    @GetMapping("/filter/price")
    public ResponseEntity<ResponseObject<List<ProductVariantDTO>>> getProductVariantsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        ResponseObject<List<ProductVariantDTO>> response = productVariantService.getProductVariantsByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/sort")
    public ResponseEntity<ResponseObject<List<ProductVariantDTO>>> getProductVariantsSortedBy(
            @RequestParam String sortBy) {
        ResponseObject<List<ProductVariantDTO>> response = productVariantService.getProductVariantsSortedBy(sortBy);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
