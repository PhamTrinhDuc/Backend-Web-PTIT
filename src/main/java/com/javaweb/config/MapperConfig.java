package com.javaweb.config;

import com.javaweb.model.ProductImageEntity;
import com.javaweb.dto.ProductDTO;
import com.javaweb.model.ProductsEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Định nghĩa Converter để ánh xạ List<ProductImageEntity> sang List<String>
        Converter<List<ProductImageEntity>, List<String>> imagePathConverter = context -> {
            List<ProductImageEntity> source = context.getSource();
            if (source == null) {
                return null;
            }
            return source.stream()
                    .map(ProductImageEntity::getImagePath)
                    .collect(Collectors.toList());
        };

        // Áp dụng Converter cho ánh xạ từ ProductsEntity sang ProductDTO
        modelMapper.createTypeMap(ProductsEntity.class, ProductDTO.class)
                .addMappings(mapper -> mapper.using(imagePathConverter)
                        .map(ProductsEntity::getProductImageEntities, ProductDTO::setImagePaths));

        return modelMapper;
    }
}