package com.javaweb.service.impl;
import com.javaweb.dto.AddToCartDTO;
import com.javaweb.exception.NotFoundException;
import com.javaweb.model.*;
import com.javaweb.repository.CartRepository;
import com.javaweb.repository.ProductVariantRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.time.LocalDateTime;


@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    public ResponseObject<String> addToCartDb(AddToCartDTO request) {
        try {
            // Kiểm tra user có tồn tại không
            UserEntity user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found with id: " + request.getUserId()));

            // Kiểm tra product variant có tồn tại không
            ProductVariantEntity variant = productVariantRepository.findById(request.getVariantId())
                    .orElseThrow(() -> new NotFoundException("Product variant not found with id: " + request.getVariantId()));

            // Kiểm tra quantity hợp lệ
            if (request.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            // Tìm hoặc tạo mới cart cho user
            CartEntity cart = cartRepository.findByUserId(request.getUserId())
                    .orElseGet(() -> {
                        CartEntity newCart = new CartEntity();
                        newCart.setUser(user);
                        newCart.setCartItems(new ArrayList<>());
                        return cartRepository.save(newCart);
                    });

            // Kiểm tra xem variant đã có trong cart chưa
            CartItemEntity existingCartItem = cart.getCartItems().stream()
                    .filter(item -> item.getVariant().getId().equals(request.getVariantId()))
                    .findFirst()
                    .orElse(null);

            if (existingCartItem != null) {
                // Nếu variant đã có, cộng thêm quantity
                existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
                existingCartItem.setAddAt(LocalDateTime.now()); // Cập nhật thời gian thêm
            } else {
                // Nếu variant chưa có, tạo mới cart item
                CartItemEntity cartItem = new CartItemEntity();
                cartItem.setCart(cart);
                cartItem.setVariant(variant);
                cartItem.setQuantity(request.getQuantity());
                cartItem.setAddAt(LocalDateTime.now());
                cart.getCartItems().add(cartItem);
            }

            // Lưu cart
            cartRepository.save(cart);

            return ResponseObject.success("Product added to cart successfully");

        } catch (NotFoundException e) {
            e.printStackTrace();
            return ResponseObject.error(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseObject.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
