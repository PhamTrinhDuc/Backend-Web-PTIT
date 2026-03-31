package com.javaweb.controller;

import com.javaweb.dto.WishlistDTO;
import com.javaweb.model.ResponseObject;
import com.javaweb.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseObject<List<WishlistDTO>>> getWishlist(@PathVariable Long userId) {
        try {
            List<WishlistDTO> wishlist = wishlistService.getWishlistByUser(userId);
            return ResponseEntity.ok(ResponseObject.success(wishlist));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseObject.error(e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }

    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<ResponseObject<WishlistDTO>> addToWishlist(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            WishlistDTO wishlistDTO = wishlistService.addToWishlist(userId, productId);
            return ResponseEntity.ok(ResponseObject.success(wishlistDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseObject.error(e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<ResponseObject<String>> removeFromWishlist(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            wishlistService.removeFromWishlist(userId, productId);
            return ResponseEntity.ok(ResponseObject.success("Product removed from wishlist"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseObject.error(e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }

    @GetMapping("/{userId}/check/{productId}")
    public ResponseEntity<ResponseObject<Boolean>> isFavorite(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            boolean favorite = wishlistService.isFavorite(userId, productId);
            return ResponseEntity.ok(ResponseObject.success(favorite));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseObject.error(e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }
}
