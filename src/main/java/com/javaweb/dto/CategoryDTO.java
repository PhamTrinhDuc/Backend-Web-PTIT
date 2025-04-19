package com.javaweb.dto;

public class CategoryDTO {
    private String slug;
    private String name;


    public CategoryDTO() {
    }
    public CategoryDTO(Long id, String slug, String name) {
        this.slug = slug;
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}