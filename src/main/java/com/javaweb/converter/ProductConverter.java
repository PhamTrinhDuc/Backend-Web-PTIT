package com.javaweb.converter;


import com.javaweb.dto.ProductDTO;
import com.javaweb.model.ProductsEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {

    // Convert từ Entity -> DTO
    public ProductDTO toDTO(ProductsEntity entity) {
        return new ProductDTO(
                entity.getId(),
                entity.getName(),
                entity.getBrand(),
                entity.getDescription()
        );
    }

    // Convert từ DTO -> Entity
    public ProductsEntity toEntity(ProductDTO dto) {
        ProductsEntity entity = new ProductsEntity();
        entity.setId(dto.getId()); // Nếu ID null, JPA sẽ tự động tạo ID mới khi insert
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}