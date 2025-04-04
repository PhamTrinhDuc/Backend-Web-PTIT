package com.javaweb.mapper;

import com.javaweb.dto.CategoryDTO;
import com.javaweb.model.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO toDTO(CategoryEntity entity);

    CategoryEntity toEntity(CategoryDTO dto);
}