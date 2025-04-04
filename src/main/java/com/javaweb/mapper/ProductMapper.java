package com.javaweb.mapper;

import com.javaweb.dto.ProductDTO;
import com.javaweb.model.ProductsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO toDTO(ProductsEntity entity);

    ProductsEntity toEntity(ProductDTO dto);
}