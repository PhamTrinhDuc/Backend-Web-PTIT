package com.javaweb.service.impl;

import com.javaweb.converter.ProductVariantConverter;
import com.javaweb.dto.ProductVariantDTO;
import com.javaweb.exception.NotFoundException;
import com.javaweb.model.CategoryEntity;
import com.javaweb.model.ProductVariantEntity;
import com.javaweb.model.ResponseObject;
import com.javaweb.repository.CategoryRepository;
import com.javaweb.repository.ProductVariantRepository;
import com.javaweb.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductVariantImpl implements ProductVariantService {
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private ProductVariantConverter productVariantConverter;
    @Autowired CategoryRepository categoryRepository;

    public ResponseObject<List<ProductVariantDTO>> getAllProductVariant() {
        try {
            List<ProductVariantEntity> productVariants = productVariantRepository.findAll();

            List<ProductVariantDTO> productVariantDTOs = productVariants.stream()
                    .map(ProductVariantDTO::new)
                    .collect(Collectors.toList());
            return ResponseObject.success(productVariantDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseObject<List<ProductVariantDTO>> getProductVariantByDiscount(){
        try {
            // Lấy danh sách ProductVariant đã sắp xếp theo discount giảm dần
            List<ProductVariantEntity> variants = productVariantRepository.findAllSortedByDiscountDesc();
            if (variants.isEmpty()) {
                throw new NotFoundException("No product variants found");
            }

            // Chuyển đổi sang DTO
            List<ProductVariantDTO> variantDTOs = variants.stream()
                    .map(ProductVariantDTO::new)
                    .toList();

            return ResponseObject.success(variantDTOs);

        } catch (NotFoundException e) {
            e.printStackTrace();
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseObject<List<ProductVariantDTO>> getProductVariantBySale(){
        return null;
    }


    public ResponseObject<ProductVariantDTO> getProductVariantById(Long id){
        try {
            ProductVariantEntity productVariantEntity = productVariantRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Product variant not found with id: " + id));
            ProductVariantDTO productVariantDTO = new ProductVariantDTO(productVariantEntity);
            return ResponseObject.success(productVariantDTO);

        } catch (NotFoundException e){
            e.printStackTrace();
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseObject<List<ProductVariantDTO>> getProductVariantsByCategorySlug(String categorySlug) {
        try {
            // Kiểm tra category có tồn tại không
            categoryRepository.findBySlug(categorySlug)
                    .orElseThrow(() -> new NotFoundException("Category not found with slug: " + categorySlug));
            // Lấy danh sách ProductVariant
            List<ProductVariantEntity> variants = productVariantRepository.findByCategorySlug(categorySlug);
            if (variants.isEmpty()) {
                throw new NotFoundException("No product variants found for category with slug: " + categorySlug);
            }
            // Chuyển đổi sang DTO
            List<ProductVariantDTO> variantDTOs = variants.stream()
                    .map(ProductVariantDTO::new)
                    .toList();
            return ResponseObject.success(variantDTOs);

        } catch (NotFoundException e) {
            e.printStackTrace();
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseObject<ProductVariantEntity> saveOrUpdateProductVariant(ProductVariantDTO productVariantDTO){
        try {
            Long id = productVariantDTO.getId();
            if(id==null && productVariantRepository.existsByName(productVariantDTO.getName())){
                return ResponseObject.error("Product variant already exists", HttpStatus.BAD_REQUEST);

            }
            ProductVariantEntity productVariantEntity;
            if(id!=null){
                productVariantEntity = productVariantRepository.findById(id)
                        .orElse(new ProductVariantEntity());
            }else{
                productVariantEntity = new ProductVariantEntity();
            }
            productVariantEntity = productVariantConverter.toEntity(productVariantDTO);
            productVariantRepository.save(productVariantEntity);
            return ResponseObject.success(productVariantEntity);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseObject.error("Failed to save or update product variant", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseObject<Void> deleteProductVariant(Long id){
        if(id == null){
            return ResponseObject.error("ID must not be null", HttpStatus.BAD_REQUEST);
        }
        if(!productVariantRepository.existsById(id)){
            return ResponseObject.error("Product variant not found", HttpStatus.NOT_FOUND);
        }
        productVariantRepository.deleteById(id);
        return ResponseObject.success(null);
    }

    // Lọc theo khoảng giá
    public ResponseObject<List<ProductVariantDTO>> getProductVariantsByPriceRange(Double minPrice, Double maxPrice) {
        try {
            // Kiểm tra giá trị đầu vào
            if (minPrice < 0 || maxPrice < minPrice) {
                throw new IllegalArgumentException("Invalid price range: minPrice must be >= 0 and <= maxPrice");
            }

            // Lấy danh sách ProductVariant
            List<ProductVariantEntity> variants = productVariantRepository.findByPriceRange(minPrice, maxPrice);
            if (variants.isEmpty()) {
                throw new NotFoundException("No product variants found in the given price range");
            }

            // Chuyển đổi sang DTO
            List<ProductVariantDTO> variantDTOs = variants.stream()
                    .map(ProductVariantDTO::new)
                    .toList();

            return ResponseObject.success(variantDTOs);

        } catch (NotFoundException e) {
            e.printStackTrace();
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseObject.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Sắp xếp theo tiêu chí
    public ResponseObject<List<ProductVariantDTO>> getProductVariantsSortedBy(String sortBy) {
        try {
            // Kiểm tra sortBy hợp lệ
            List<String> validSortOptions = List.of("newest", "price_asc", "price_desc", "name_asc", "rating");
            if (!validSortOptions.contains(sortBy)) {
                throw new IllegalArgumentException("Invalid sortBy value. Must be one of: " + validSortOptions);
            }

            // Lấy danh sách ProductVariant
            List<ProductVariantEntity> variants = productVariantRepository.findAllSortedBy(sortBy);
            if (variants.isEmpty()) {
                throw new NotFoundException("No product variants found");
            }

            // Chuyển đổi sang DTO
            List<ProductVariantDTO> variantDTOs = variants.stream()
                    .map(ProductVariantDTO::new)
                    .toList();

            return ResponseObject.success(variantDTOs);

        } catch (NotFoundException e) {
            e.printStackTrace();
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseObject.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}