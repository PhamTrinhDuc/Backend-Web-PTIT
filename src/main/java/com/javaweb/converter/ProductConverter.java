package com.javaweb.converter;

import com.javaweb.dto.ProductDTO;
import com.javaweb.model.ProductsEntity;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;


@Component
public class ProductConverter {

    private final ModelMapper modelMapper;

    public ProductConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Chuyển từ Entity sang DTO
    public ProductDTO toDTO(ProductsEntity productEntity) {
        return modelMapper.map(productEntity, ProductDTO.class);
    }

    // Chuyển từ DTO sang Entity
    public ProductsEntity toEntity(ProductDTO productDTO) {
        return modelMapper.map(productDTO, ProductsEntity.class);
    }
}
