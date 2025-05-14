package com.javaweb.controller;

import com.javaweb.dto.ChangePasswordRequest;
import com.javaweb.dto.RegisterRequestDTO;
import com.javaweb.dto.UpdateUserRequestDTO;
import com.javaweb.dto.UserDTO;
import com.javaweb.model.UserEntity;
import com.javaweb.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserEntity> users = userService.getAllUsers();
            List<UserDTO> userDTOs = users.stream()
                    .map(UserDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/me/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateUserRequestDTO request) {
        try{
            UserEntity user = userService.updateProfile(request);
            UserDTO userDTO = new UserDTO(user);
            return ResponseEntity.ok(userDTO);
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/me/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("Account deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(request);
            return ResponseEntity.ok("Password updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}