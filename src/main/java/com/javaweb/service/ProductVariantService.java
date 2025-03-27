package com.javaweb.service;

import com.javaweb.dto.ProductDTO;
import com.javaweb.dto.ProductVariantDTO;
import com.javaweb.model.ProductVariantEntity;
import com.javaweb.model.ResponseObject;

import java.util.List;

public interface ProductVariantService {
      ResponseObject<List<ProductVariantDTO>> getAllProductVariant();
      ResponseObject<ProductVariantDTO> getProductVariantById(Long id);
      ResponseObject<ProductVariantEntity> saveOrUpdateProductVariant(ProductVariantDTO productVariantDTO);
      ResponseObject<Void> deleteProductVariant(Long id);
      ResponseObject<List<ProductVariantDTO>> getProductVariantByDiscount();
      ResponseObject<List<ProductVariantDTO>> getProductVariantBySale();
      ResponseObject<List<ProductVariantDTO>> getProductVariantsByPriceRange(Double minPrice, Double maxPrice);
      ResponseObject<List<ProductVariantDTO>> getProductVariantsSortedBy(String sortBy);
}

