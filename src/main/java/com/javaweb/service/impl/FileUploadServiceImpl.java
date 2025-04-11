package com.javaweb.service.impl;

import com.javaweb.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload-dir:uploads/}")
    private String uploadDir;

    @Value("${server.base-url:http://localhost:8080}")
    private String baseUrl;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif"};

    @Override
    public String uploadImage(MultipartFile file) throws Exception {
        // Kiểm tra file rỗng
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Kiểm tra kích thước file
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds limit of 5MB");
        }

        // Kiểm tra định dạng file
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !hasValidExtension(originalFilename)) {
            throw new IllegalArgumentException("Invalid file type. Only JPG, JPEG, PNG, GIF are allowed");
        }

        // Tạo tên file duy nhất
        String filename = UUID.randomUUID() + "_" + originalFilename;
        Path path = Paths.get(uploadDir, filename);

        try {
            // Tạo thư mục nếu chưa tồn tại
            Files.createDirectories(path.getParent());

            // Lưu file
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Tạo và trả về URL
            return baseUrl + "/uploads/" + filename;
        } catch (IOException e) {
            throw new Exception("Failed to upload file: " + e.getMessage(), e);
        }
    }

    private boolean hasValidExtension(String filename) {
        String lowercaseFilename = filename.toLowerCase();
        for (String ext : ALLOWED_EXTENSIONS) {
            if (lowercaseFilename.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}