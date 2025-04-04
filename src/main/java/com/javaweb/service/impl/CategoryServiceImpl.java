package com.javaweb.service.impl;

import com.javaweb.dto.CategoryDTO;
import com.javaweb.dto.ProductDTO;
import com.javaweb.model.CategoryEntity;
import com.javaweb.repository.CategoryRepository;
import com.javaweb.service.CategoryService;
import com.javaweb.utils.SlugUtil;
import org.modelmapper.ModelMapper;
import com.javaweb.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.javaweb.model.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.javaweb.mapper.CategoryMapper;
import java.util.List;
import java.util.stream.Collectors;


@Service // đánh dấu bean service.
public class CategoryServiceImpl implements CategoryService {
    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseObject<List<CategoryDTO>> findAllCategory() {
        try {
            List<CategoryEntity> categories = categoryRepository.findAll();

            List<CategoryDTO> categoryDTO = categories.stream()
                    .map(category -> modelMapper.map(category, CategoryDTO.class))
                    .collect(Collectors.toList());

            return ResponseObject.success(categoryDTO);
        } catch (Exception e) {
            log.error("Error in findAllCategory: {}", e.getMessage(), e);
            return ResponseObject.error("Failed to fetch categories", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseObject<CategoryDTO> findCategoryBySlug(String slug) {
        try {
            CategoryEntity categoryEntity = categoryRepository.findBySlug(slug)
                    .orElseThrow(() -> new NotFoundException("Category not found with slug: " + slug));

            CategoryDTO categoryDTO = modelMapper.map(categoryEntity, CategoryDTO.class);
            return ResponseObject.success(categoryDTO);

        } catch (NotFoundException e) {
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error in findCategoryBySlug: {}", e.getMessage(), e);
            return ResponseObject.error("Failed to fetch category by slug", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseObject<CategoryEntity> saveOrUpdateCategory(CategoryDTO categoryDTO) {
        try {
            Long id = categoryDTO.getId();
            if (id == null && categoryRepository.existsByName(categoryDTO.getName())) {
                return ResponseObject.error("Category already exists", HttpStatus.BAD_REQUEST);
            }

            CategoryEntity categoryEntity;
            if (categoryDTO.getId() != null) {
                categoryEntity = categoryRepository.findById(categoryDTO.getId())
                        .orElse(new CategoryEntity());
            } else {
                categoryEntity = new CategoryEntity();
            }
            // Manual mapping from DTO to Entity
            categoryEntity.setId(categoryDTO.getId());
            categoryEntity.setName(categoryDTO.getName());
            categoryEntity.setSlug(categoryDTO.getSlug());
            // Map other fields
            categoryRepository.save(categoryEntity);
            return ResponseObject.success(categoryEntity);

        } catch (Exception e) {
            log.error("Error in saveOrUpdateCategory: {}", e.getMessage(), e);
            return ResponseObject.error("Failed to save or update category", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseObject<Void> deleteCategory(Long id) {
        if (id == null) {
            return ResponseObject.error("ID must not be null", HttpStatus.BAD_REQUEST);
        }
        if (!categoryRepository.existsById(id)) {
            return ResponseObject.error("Category not found", HttpStatus.NOT_FOUND);
        }
        categoryRepository.deleteById(id);
        return ResponseObject.success(null);
    }
}
