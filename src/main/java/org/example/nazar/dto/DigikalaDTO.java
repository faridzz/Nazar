package org.example.nazar.dto;

import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
public class DigikalaDTO extends BaseDTO {
    private int id; // شناسه محصول
    private String image; // لینک تصویر محصول
    private String url; // لینک مشخصات محصول

    @Override
    public String toString() {
        return "DigikalaDTO{" +
                "id=" + id +
                ", name='" + getTitle() + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +

                '}';
    }
}
