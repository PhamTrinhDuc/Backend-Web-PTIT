package com.javaweb.dto;

public class BannerDTO {
    private Long id;
    private String title;
    private String discount;
    private String image;

    public BannerDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDiscount() { return discount; }
    public void setDiscount(String discount) { this.discount = discount; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
