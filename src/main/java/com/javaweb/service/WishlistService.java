package com.javaweb.service;

import com.javaweb.dto.WishlistDTO;
import java.util.List;

public interface WishlistService {

    WishlistDTO addToWishlist(Long userId, Long productId);

    void removeFromWishlist(Long userId, Long productId);

    List<WishlistDTO> getWishlistByUser(Long userId);

    boolean isFavorite(Long userId, Long productId);
}
