package com.javaweb.repository;

import com.javaweb.model.ProductDTO;
import com.javaweb.repository.entity.ProductEntity;
import java.util.List;

public interface ProductRepository {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String DB_USER = "postgres";
    static final String DB_PASSWORD = "duc8504@@";

    List<ProductEntity> getProductByParams(ProductDTO productParams);
}
