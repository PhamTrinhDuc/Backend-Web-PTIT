package com.javaweb.service.impl;

import com.javaweb.dto.CategoryDTO;
import com.javaweb.model.CategoryEntity;
import com.javaweb.repository.CategoryRepository;
import com.javaweb.service.CategoryService;
import com.javaweb.utils.SlugUtil;
import com.javaweb.exception.NotFoundException;
import com.javaweb.converter.CategoryConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.javaweb.model.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;


@Service // đánh dấu bean service.
public class CategoryServiceImpl implements CategoryService {
    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryConverter categoryConverter;


    public ResponseObject<List<CategoryDTO>> findAllCategory() {
        try {
            List<CategoryEntity> categories = categoryRepository.findAll();

            List<CategoryDTO> categoryConverted = categories.stream()
                    .map(categoryConverter::toDTO)
                    .collect(Collectors.toList());

            return ResponseObject.success(categoryConverted);
        } catch (Exception e) {
            log.error("Error in findAllCategory: {}", e.getMessage(), e);
            return ResponseObject.error("Failed to fetch categories", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseObject<CategoryDTO> findCategoryById(Long id) {
        try {
            CategoryEntity categoryEntity = categoryRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));

            CategoryDTO categoryDTO = categoryConverter.toDTO(categoryEntity);

            return ResponseObject.success(categoryDTO);

        } catch (NotFoundException e) {
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.error("Failed to fetch category by id", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseObject<CategoryEntity> saveOrUpdateCategory(CategoryDTO categoryDTO) {
        try {
            Long id = categoryDTO.getId();
            if (id == null && categoryRepository.existsByName(categoryDTO.getName())) {
                return ResponseObject.error("Product already exists", HttpStatus.BAD_REQUEST);
            }
            CategoryEntity categoryEntity;

            if (categoryDTO.getId() != null) {
                categoryEntity = categoryRepository.findById(categoryDTO.getId())
                        .orElse(new CategoryEntity()); // Nếu không có, tạo mới
            } else {
                categoryEntity = new CategoryEntity(); // Tạo mới
            }
            categoryEntity = categoryConverter.toEntity(categoryDTO);

            categoryRepository.save(categoryEntity);
            return ResponseObject.success(categoryEntity);

        } catch (Exception e) {
            e.printStackTrace();
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
