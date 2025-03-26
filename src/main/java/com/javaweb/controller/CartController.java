package com.javaweb.controller;

import com.javaweb.dto.AddToCartDTO;
import com.javaweb.model.ResponseObject;
import com.javaweb.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartServiceImpl cartService;

    @PostMapping("/cart/add")
    public ResponseEntity<ResponseObject<String>> addToCart(@RequestBody AddToCartDTO request) {
        ResponseObject<String> response = cartService.addToCartDb(request);
        return new ResponseEntity<>(response, response.getStatus());
    }
}