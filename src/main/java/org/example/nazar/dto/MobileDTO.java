package org.example.nazar.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// کلاس DTO برای نمایش نتایج جستجو
@Getter
@Setter
@NoArgsConstructor
public class MobileDTO extends BaseDTO {
    public MobileDTO(BaseDTO baseDTO) {
        super(baseDTO.getId(), baseDTO.getTitle(), baseDTO.getImage(), baseDTO.getUrl());
    }

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