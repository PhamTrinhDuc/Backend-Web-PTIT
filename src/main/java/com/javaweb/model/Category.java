package com.javaweb.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity //  đánh dấu đây là 1 Entity
@Table(name = "category") // xác định tên bảng trong database
@Data // tự động tạo getter, setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(name = "name", nullable = false) // ánh xạ tới cột trong bảng
    private String name;

    @Column(name = "description")
    private String description;
}


