package com.javaweb.service;

import com.javaweb.dto.CategoryDTO;
import com.javaweb.model.CategoryEntity;
import com.javaweb.model.ResponseObject;


import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAllCategory();
    ResponseObject<CategoryEntity> saveOrUpdateCategory(CategoryDTO categoryDTO);
    ResponseObject<Void> deleteCategory(Long id);
}