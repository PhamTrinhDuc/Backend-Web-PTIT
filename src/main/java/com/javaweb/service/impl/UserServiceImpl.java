package com.javaweb.service.impl;

import com.javaweb.dto.RegisterRequestDTO;
import com.javaweb.dto.UpdateUserRequestDTO;
import com.javaweb.model.UserEntity;
import com.javaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class UserServiceImpl {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserEntity registerUser(RegisterRequestDTO request){
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Tạo User
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Mã hóa mật khẩu
        user.setEmail(request.getEmail());
        user.setRole("user"); // Mặc định là user

        // Tạo UserProfile
        user.setFullname(null);
        user.setAvatar(null);
        user.setBirthday(null);
        user.setGender(null);
        user.setAddress(null);
        user.setStatus("active");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity updateProfile(String userName, UpdateUserRequestDTO request){
        UserEntity user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kiểm tra status
        if ("inactive".equals(user.getStatus())) {
            throw new RuntimeException("Account is inactive");
        }

        // Cập nhật các trường nếu có giá trị mới
        if (request.getFullname() != null) {
            user.setFullname(request.getFullname());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getBirthday() != null) {
            user.setBirthday(request.getBirthday());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void deleteUser(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user == null) {
            throw new RuntimeException("User profile not found");
        }
        // Đánh dấu tài khoản là inactive (soft delete)
        user.setStatus("inactive");
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}