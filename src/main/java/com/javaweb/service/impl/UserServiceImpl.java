package com.javaweb.service.impl;

import com.javaweb.dto.RegisterRequestDTO;
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
        userProfile.setFullname(request.getFullname());
        userProfile.setAvatar(request.getAvatar() != null ? request.getAvatar() : "/avatars/default.jpg");
        userProfile.setBirthday(request.getBirthday());
        userProfile.setGender(request.getGender());
        userProfile.setAddress(request.getAddress());
        userProfile.setStatus("active");
        userProfile.setCreatedAt(LocalDateTime.now());
        userProfile.setUpdatedAt(LocalDateTime.now());
        userProfile.setUser(user);

        user.setUserProfile(userProfile);
        return userRepository.save(user);
    }
}
