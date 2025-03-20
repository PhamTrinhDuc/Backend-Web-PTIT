package com.javaweb.utils;

// SlugUtil.java
public class SlugUtil {
    public static String createSlug(String input) {
        if (input == null) return "";
        // Chuyển thành lowercase, loại bỏ dấu, thay khoảng trắng bằng dấu gạch nối
        String slug = input.toLowerCase()
                .replaceAll("[áàảãạăắằẳẵặâấầẩẫậ]", "a")
                .replaceAll("[éèẻẽẹêếềểễệ]", "e")
                .replaceAll("[íìỉĩị]", "i")
                .replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o")
                .replaceAll("[úùủũụưứừửữự]", "u")
                .replaceAll("[ýỳỷỹỵ]", "y")
                .replaceAll("[đ]", "d")
                .replaceAll("[^a-z0-9\\s-]", "") // Loại bỏ ký tự đặc biệt
                .trim()
                .replaceAll("\\s+", "-"); // Thay khoảng trắng bằng dấu gạch nối
        return slug;
    }
}