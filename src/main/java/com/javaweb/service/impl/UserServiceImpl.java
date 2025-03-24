package com.javaweb.service.impl;

import com.javaweb.dto.RegisterRequestDTO;
import com.javaweb.dto.UpdateProfileRequestDTO;
import com.javaweb.model.UserEntity;
import com.javaweb.model.UserProfileEntity;
import com.javaweb.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        // Kiểm tra status của tài khoản
        UserProfileEntity userProfile = user.getUserProfile();
        if (userProfile == null || "inactive".equals(userProfile.getStatus())) {
            throw new UsernameNotFoundException("Account is inactive");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

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
        UserProfileEntity userProfile = new UserProfileEntity();
        userProfile.setFullname(null);
        userProfile.setAvatar(null);
        userProfile.setBirthday(null);
        userProfile.setGender(null);
        userProfile.setAddress(null);
        userProfile.setStatus("active");
        userProfile.setCreatedAt(LocalDateTime.now());
        userProfile.setUpdatedAt(LocalDateTime.now());
        userProfile.setUser(user);

        user.setUserProfile(userProfile);
        return userRepository.save(user);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity updateProfile(String userName, UpdateProfileRequestDTO request){
        UserEntity user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserProfileEntity userProfile = user.getUserProfile();
        if(userProfile == null) {
            userProfile = new UserProfileEntity();
            userProfile.setUser(user);
            userProfile.setCreatedAt(LocalDateTime.now());
            user.setUserProfile(userProfile);
        }

        // Kiểm tra status
        if ("inactive".equals(userProfile.getStatus())) {
            throw new RuntimeException("Account is inactive");
        }

        // Cập nhật các trường nếu có giá trị mới
        if (request.getFullname() != null) {
            userProfile.setFullname(request.getFullname());
        }
        if (request.getAvatar() != null) {
            userProfile.setAvatar(request.getAvatar());
        }
        if (request.getBirthday() != null) {
            userProfile.setBirthday(request.getBirthday());
        }
        if (request.getGender() != null) {
            userProfile.setGender(request.getGender());
        }
        if (request.getAddress() != null) {
            userProfile.setAddress(request.getAddress());
        }
        userProfile.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public void deleteUser(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileEntity userProfile = user.getUserProfile();
        if (userProfile == null) {
            throw new RuntimeException("User profile not found");
        }

        // Đánh dấu tài khoản là inactive (soft delete)
        userProfile.setStatus("inactive");
        userProfile.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }
}
