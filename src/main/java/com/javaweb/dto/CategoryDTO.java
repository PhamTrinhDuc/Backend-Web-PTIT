package com.javaweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryDTO {
    private Long id;
    private String slug;
    private String name;
    private String description;

    private boolean isActive;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String slug, String name, String description, boolean isActive) {
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("isActive")
    public boolean getIsActive() {
        return isActive;
    }

    @JsonProperty("isActive")
    public void setActive(boolean active) {
        this.isActive = active;
    }
}