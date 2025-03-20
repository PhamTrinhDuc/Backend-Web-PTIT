package com.javaweb.converter;


import com.javaweb.dto.CategoryDTO;
import com.javaweb.model.CategoryEntity;
import org.springframework.stereotype.Component;
import com.javaweb.utils.SlugUtil;

@Component
public class CategoryConverter {

    // Convert từ Entity -> DTO
    public CategoryDTO toDTO(CategoryEntity entity) {
        return new CategoryDTO(
                entity.getId(),
                entity.getSlug(),
                entity.getName(),
                entity.getDescription(),
                entity.getIsActive()
        );
    }

    // Convert từ DTO -> Entity
    public CategoryEntity toEntity(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(dto.getId()); // Nếu ID null, JPA sẽ tự động tạo ID mới khi insert
        if( dto.getSlug() == null){
            String slug = SlugUtil.createSlug(dto.getName());
            dto.setSlug(slug);
        }
        entity.setSlug(dto.getSlug());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }
}