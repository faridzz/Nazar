package org.example.nazar.dto;

import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
public class DigikalaDTO extends BaseDTO {


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
