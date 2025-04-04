package com.javaweb.service;

import com.javaweb.dto.CategoryDTO;
import com.javaweb.model.CategoryEntity;
import com.javaweb.model.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import java.util.List;

public interface CategoryService {
    ResponseObject<List<CategoryDTO>> findAllCategory();
    ResponseObject<CategoryDTO> findCategoryBySlug(String slug);
    ResponseObject<CategoryEntity> saveOrUpdateCategory(CategoryDTO categoryDTO);
    ResponseObject<Void> deleteCategory(Long id);
}