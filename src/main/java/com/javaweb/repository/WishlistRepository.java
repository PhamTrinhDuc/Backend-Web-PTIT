package com.javaweb.repository;

import com.javaweb.model.ProductsEntity;
import com.javaweb.model.UserEntity;
import com.javaweb.model.WishlistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {

    List<WishlistEntity> findByUser(UserEntity user);

    Optional<WishlistEntity> findByUserAndProduct(UserEntity user, ProductsEntity product);

    @Modifying
    @Transactional
    void deleteByUserAndProduct(UserEntity user, ProductsEntity product);

    boolean existsByUserAndProduct(UserEntity user, ProductsEntity product);
}
