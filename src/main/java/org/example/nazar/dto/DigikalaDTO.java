package org.example.nazar.dto;

import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@NoArgsConstructor
public class DigikalaDTO extends BaseDTO {
public DigikalaDTO(BaseDTO baseDTO) {
        super(baseDTO.getId(), baseDTO.getTitle(), baseDTO.getImage(), baseDTO.getUrl());
    }

    @Override
    public String toString() {
        return "DigikalaDTO{" +
                "id=" + getId() +
                ", name='" + getTitle() + '\'' +
                ", image='" + getImage() + '\'' +
                ", url='" + getId() + '\'' +

                '}';
    }
}
