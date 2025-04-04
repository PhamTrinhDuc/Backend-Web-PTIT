package com.javaweb.repository;

import com.javaweb.dto.CategoryDTO;
import com.javaweb.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByName(String name);
    boolean existsById(Long id);
    Optional<CategoryEntity> findBySlug(String slug);
}

