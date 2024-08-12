package org.example.nazar.dto;

import lombok.Getter;
import lombok.Setter;

// کلاس DTO برای نمایش نتایج جستجو
@Getter
@Setter
public class MobileDTO extends BaseDTO {
    private int id; // شناسه محصول
    private String image; // لینک تصویر محصول
    private String url; // لینک مشخصات محصول

    @Override
    public String toString() {
        return "MobileDTO{" +
                "id=" + id +
                ", name='" + getTitle() + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +

                '}';
    }
}