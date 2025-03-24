package com.javaweb.converter;

import com.javaweb.dto.ProductVariantDTO;
import com.javaweb.model.ProductVariantEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class ProductVariantConverter {

    private final ModelMapper modelMapper;

    public ProductVariantConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProductVariantDTO toDTO(ProductVariantEntity entity) {
        return modelMapper.map(entity, ProductVariantDTO.class);
    }

    public ProductVariantEntity toEntity(ProductVariantDTO dto){
        return modelMapper.map(dto, ProductVariantEntity.class);
    }
}
