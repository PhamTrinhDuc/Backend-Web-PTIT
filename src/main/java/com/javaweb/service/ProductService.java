package com.javaweb.service;

import com.javaweb.dto.UpdateProductRequestDTO;
import com.javaweb.model.ProductsEntity;
import com.javaweb.dto.ProductDTO;
import com.javaweb.model.ResponseObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;


public interface ProductService {
    ResponseObject<Page<ProductDTO>> findAllProducts (Pageable pageable);
    ResponseObject<ProductDTO> findProductById (Long id);
    ResponseObject<ProductsEntity> updateProduct (UpdateProductRequestDTO productDTO);
    ResponseObject<Void> deleteProduct (Long id);
    ResponseObject<List<ProductDTO>> findProductByDiscount();
    ResponseObject<List<ProductDTO>> findProductByPriceRange(Double minPrice, Double maxPrice);
    ResponseObject<List<ProductDTO>> findProductsSortedBy(String sortBy);
}
