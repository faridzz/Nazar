package org.example.nazar.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class BaseDTO {
    private String title; // عنوان محصول
    @Setter
    private Long id; // شناسه محصول
    private String image; // لینک تصویر محصول
    private String url; // لینک مشخصات محصول

    public BaseDTO(Long id, String title, String image, String url) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title.toLowerCase().trim();
    }

    public void setImage(String image) {
        this.image = image.toLowerCase().trim();
    }

    public void setUrl(String url) {
        this.url = url.toLowerCase().trim();
    }
}
