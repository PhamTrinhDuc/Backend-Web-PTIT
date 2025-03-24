package com.javaweb.service;

import com.javaweb.dto.ProductDTO;
import com.javaweb.dto.ProductVariantDTO;
import com.javaweb.model.ProductVariantEntity;
import com.javaweb.model.ResponseObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductVariantService {
      ResponseObject<Page<ProductVariantDTO>> getAllProductVariant(Pageable pageable);
      ResponseObject<ProductVariantDTO> getProductVariantById(Long id);
      ResponseObject<ProductVariantEntity> saveOrUpdateProductVariant(ProductVariantDTO productVariantDTO);
      ResponseObject<Void> deleteProductVariant(Long id);
}
