package org.example.nazar.dto;

import lombok.Data;

@Data
public class BaseDTO {
    private String title; // عنوان محصول
    private int id; // شناسه محصول
    private String image; // لینک تصویر محصول
    private String url; // لینک مشخصات محصول
}
