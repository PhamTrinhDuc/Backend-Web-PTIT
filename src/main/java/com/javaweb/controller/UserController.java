package com.javaweb.controller;

import com.javaweb.dto.*;
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

    @GetMapping("/top-spending")
    public ResponseEntity<List<TopSpendingUserDTO>> getTop5SpendingUsers() {
        List<UserSpendingDTO> topSpendingUsers = userService.getTop5SpendingUsers();

        // Chuyển đổi sang DTO để trả về client
        List<TopSpendingUserDTO> response = topSpendingUsers.stream()
                .map(dto -> new TopSpendingUserDTO(
                        dto.getUser().getId(),
                        dto.getUser().getUsername(),
                        dto.getUser().getFullname(),
                        dto.getUser().getEmail(),
                        dto.getTotalSpending()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}