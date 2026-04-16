package com.javaweb.controller;

import com.javaweb.model.FlashSaleEntity;
import com.javaweb.model.ProductsEntity;
import com.javaweb.repository.FlashSaleRepository;
import com.javaweb.dto.FlashSaleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flashsales")
public class FlashSaleController {

    private final FlashSaleRepository flashSaleRepository;
    private final com.javaweb.repository.ProductRepository productRepository;

    public FlashSaleController(FlashSaleRepository flashSaleRepository, com.javaweb.repository.ProductRepository productRepository) {
        this.flashSaleRepository = flashSaleRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/active")
    public ResponseEntity<FlashSaleDTO> getActiveFlashSale() {
        return flashSaleRepository.findLatestActiveFlashSale()
                .map(sale -> ResponseEntity.ok(new FlashSaleDTO(sale)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<FlashSaleDTO> getAllFlashSales() {
        return flashSaleRepository.findAll().stream()
                .map(FlashSaleDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<FlashSaleDTO> createFlashSale(@RequestBody com.javaweb.dto.FlashSaleRequestDTO request) {
        FlashSaleEntity sale = new FlashSaleEntity();
        mapRequestToEntity(request, sale);
        sale.setCreatedAt(LocalDateTime.now());
        sale.setStatus(request.getStatus() != null ? request.getStatus() : "active");
        FlashSaleEntity saved = flashSaleRepository.save(sale);
        return ResponseEntity.ok(new FlashSaleDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashSaleDTO> updateFlashSale(@PathVariable Long id, @RequestBody com.javaweb.dto.FlashSaleRequestDTO request) {
        return flashSaleRepository.findById(id)
                .map(sale -> {
                    mapRequestToEntity(request, sale);
                    sale.setUpdatedAt(LocalDateTime.now());
                    FlashSaleEntity updated = flashSaleRepository.save(sale);
                    return ResponseEntity.ok(new FlashSaleDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private void mapRequestToEntity(com.javaweb.dto.FlashSaleRequestDTO request, FlashSaleEntity sale) {
        sale.setTitle(request.getTitle());
        sale.setDescription(request.getDescription());
        sale.setStartDate(request.getStartDate());
        sale.setEndDate(request.getEndDate());
        if (request.getStatus() != null) sale.setStatus(request.getStatus());
        
        if (request.getProductIds() != null) {
            java.util.List<ProductsEntity> products = productRepository.findAllById(request.getProductIds());
            sale.setProducts(products);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashSale(@PathVariable Long id) {
        if (flashSaleRepository.existsById(id)) {
            flashSaleRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
