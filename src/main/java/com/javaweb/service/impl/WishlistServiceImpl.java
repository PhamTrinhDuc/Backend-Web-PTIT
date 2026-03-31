package com.javaweb.service.impl;

import com.javaweb.dto.WishlistDTO;
import com.javaweb.model.ProductsEntity;
import com.javaweb.model.UserEntity;
import com.javaweb.model.WishlistEntity;
import com.javaweb.repository.ProductRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.repository.WishlistRepository;
import com.javaweb.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public WishlistDTO addToWishlist(Long userId, Long productId) {
        System.out.println("Processing addToWishlist for user: " + userId + " and product: " + productId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ProductsEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        System.out.println("User and product found. Checking for existing favorite...");
        if (wishlistRepository.existsByUserAndProduct(user, product)) {
             System.out.println("Product already in wishlist.");
             throw new RuntimeException("Product already in wishlist");
        }

        WishlistEntity wishlist = new WishlistEntity();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlist.setCreatedAt(LocalDateTime.now());

        System.out.println("Saving wishlist entity...");
        try {
            wishlist = wishlistRepository.save(wishlist);
            System.out.println("Wishlist saved successfully with ID: " + wishlist.getId());
        } catch (Exception e) {
            System.out.println("Error while saving wishlist: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return convertToDTO(wishlist);
    }

    @Override
    public void removeFromWishlist(Long userId, Long productId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ProductsEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        wishlistRepository.deleteByUserAndProduct(user, product);
    }

    @Override
    public List<WishlistDTO> getWishlistByUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return wishlistRepository.findByUser(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFavorite(Long userId, Long productId) {
        if (userId == null) return false;
        UserEntity user = userRepository.findById(userId).orElse(null);
        ProductsEntity product = productRepository.findById(productId).orElse(null);
        if (user == null || product == null) return false;
        return wishlistRepository.existsByUserAndProduct(user, product);
    }

    private WishlistDTO convertToDTO(WishlistEntity wishlist) {
        WishlistDTO dto = new WishlistDTO();
        dto.setId(wishlist.getId());
        dto.setUserId(wishlist.getUser().getId());
        dto.setProductId(wishlist.getProduct().getId());
        dto.setProductName(wishlist.getProduct().getName());
        dto.setProductPrice(wishlist.getProduct().getPrice());
        dto.setCreatedAt(wishlist.getCreatedAt());
        
        if (wishlist.getProduct().getProductImageEntities() != null && !wishlist.getProduct().getProductImageEntities().isEmpty()) {
            dto.setProductThumbnail(wishlist.getProduct().getProductImageEntities().get(0).getImagePath());
        }
        
        return dto;
    }
}
