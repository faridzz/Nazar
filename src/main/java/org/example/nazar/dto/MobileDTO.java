package org.example.nazar.dto;

import lombok.Getter;
import lombok.Setter;

// کلاس DTO برای نمایش نتایج جستجو
@Getter
@Setter
public class MobileDTO extends BaseDTO {


    @Override
    public String toString() {
        return "MobileDTO{" +
                "id=" + getId() +
                ", name='" + getTitle() + '\'' +
                ", image='" + getImage() + '\'' +
                ", url='" + getUrl() + '\'' +

                '}';
    }
}