package com.javaweb.service;

import com.javaweb.dto.AddToCartDTO;
import com.javaweb.model.ResponseObject;

public interface CartService {
    ResponseObject<String> addToCartDb(AddToCartDTO request);
}
