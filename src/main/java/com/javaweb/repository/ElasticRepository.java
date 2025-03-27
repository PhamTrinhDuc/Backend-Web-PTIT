package com.javaweb.repository;


import com.javaweb.model.ProductIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElasticRepository extends ElasticsearchRepository<ProductIndex, Long> {
    List<ProductIndex> findByNameContaining(String name);
    List<ProductIndex> findByPriceBetween(Double minPrice, Double maxPrice);
    List<ProductIndex> findByCategoryAndPriceLessThan(String category, Double price);
}
