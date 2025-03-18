package com.javaweb.repository.impl;

import com.javaweb.model.ProductDTO;
import com.javaweb.repository.ProductRepository;
import com.javaweb.repository.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public List<ProductEntity> getProductByParams(ProductDTO productParams) {
        List<ProductEntity> productEntityList = new ArrayList<>();
        String productName = productParams.getProductName();
        Float price = productParams.getPrice();

        // Xây dựng query động
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (productName != null && !productName.isEmpty()) {
            sql.append(" AND product_name ILIKE ?");
            params.add("%" + productName + "%"); // ILIKE cho tìm kiếm không phân biệt hoa/thường
        }
        if (price != null) {
            sql.append(" AND price >= ?");
            params.add(price);
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Set giá trị cho PreparedStatement
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof String) {
                    stmt.setString(i + 1, (String) params.get(i));
                } else if (params.get(i) instanceof Float) {
                    stmt.setFloat(i + 1, (Float) params.get(i));
                }
            }
//            System.out.println("SQL Query: " + sql.toString());
//            System.out.println("Params: " + params);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProductEntity productEntity = new ProductEntity();
                    productEntity.setProductName(rs.getString("product_name"));
                    productEntity.setPrice(rs.getFloat("price"));
                    productEntity.setDescription(rs.getString("description"));
                    productEntityList.add(productEntity);
                }
            } // ResultSet tự động đóng khi kết thúc khối `try-with-resources`

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi kết nối PostgreSQL...");
        }
        return productEntityList;
    }
}
