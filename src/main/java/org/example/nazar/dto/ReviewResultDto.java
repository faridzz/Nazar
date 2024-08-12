package org.example.nazar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class ReviewResultDto {

    private Long reviewsNumber;
    private int duplicateCount;

}
