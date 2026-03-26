package com.javaweb.service.impl;

import com.javaweb.dto.ChangePasswordRequest;
import com.javaweb.dto.RegisterRequestDTO;
import com.javaweb.dto.UpdateUserRequestDTO;
import com.javaweb.dto.UserSpendingDTO;
import com.javaweb.model.OrderEntity;
import com.javaweb.model.UserEntity;
import com.javaweb.repository.OrderRepository;
import com.javaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new User(user.getUsername(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAllActiveUsers();
    }

    public UserEntity registerUser(RegisterRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Kiểm tra password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu không khớp!");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail("");
        user.setRole("user");
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

    public UserEntity processGoogleLogin(String email, String name, String picture) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            return userOpt.get(); // Trả về user nếu đã tồn tại
        }
        
        // Tạo tài khoản mới từ Google Info
        UserEntity user = new UserEntity();

        // Safe truncation to avoid VARCHAR(255) database errors
        String baseName = email != null && email.contains("@") ? email.split("@")[0] : "google_user";
        if (baseName.length() > 50) baseName = baseName.substring(0, 50);
        String generatedUsername = baseName + "_gss" + System.currentTimeMillis();
        
        user.setUsername(generatedUsername.length() > 255 ? generatedUsername.substring(0, 255) : generatedUsername);
        user.setPassword(passwordEncoder.encode("G_SSO_DUMMY_" + System.currentTimeMillis())); 
        user.setEmail(email != null && email.length() > 255 ? email.substring(0, 255) : email);
        user.setRole("user");
        user.setFullname(name != null && name.length() > 255 ? name.substring(0, 255) : name);
        
        // Google picture URLs can sometimes exceed 255 chars. If truncated, URL breaks. Better to set null.
        if (picture != null && picture.length() > 255) {
            user.setAvatar(null);
        } else {
            user.setAvatar(picture);
        }

        user.setStatus("active");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity updateProfile(UpdateUserRequestDTO request) {
        UserEntity user = userRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if ("inactive".equals(user.getStatus())) {
            throw new RuntimeException("Account is inactive");
        }
        if (request.getFullName() != null) user.setFullname(request.getFullName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getBirthday() != null) user.setBirthday(request.getBirthday());
        if (request.getEmail() != null)  user.setEmail(request.getEmail());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getAvatar() != null) user.setAvatar(request.getAvatar());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus("inactive");
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void changePassword(ChangePasswordRequest request) {
        Long userId = request.getUserId();

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Kiểm tra xác nhận mật khẩu
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        // Cập nhật mật khẩu
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public List<UserSpendingDTO> getTop5SpendingUsers() {
        // Lấy tất cả người dùng active
        List<UserEntity> activeUsers = userRepository.findAllActiveUsers();

        // Tính tổng chi tiêu cho mỗi người dùng
        List<UserSpendingDTO> userSpendings = activeUsers.stream().map(user -> {
            // Lấy tất cả đơn hàng của người dùng không bị hủy
            List<OrderEntity> userOrders = orderRepository.findByUserIdAndStatusNot(user.getId(), "CANCELLED");
            System.out.println("order: " + userOrders);

            // Tính tổng chi tiêu
            double totalSpending = userOrders.stream()
                    .mapToDouble(OrderEntity::getTotalAmount)
                    .sum();

            return new UserSpendingDTO(user, totalSpending);
        })
        // Sắp xếp theo tổng chi tiêu giảm dần
        .sorted((a, b) -> Double.compare(b.getTotalSpending(), a.getTotalSpending()))
        // Lấy top 5
        .limit(5)
        .collect(Collectors.toList());
        return userSpendings;
    }
}