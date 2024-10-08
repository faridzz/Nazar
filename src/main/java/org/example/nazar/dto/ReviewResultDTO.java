package org.example.nazar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class ReviewResultDTO {

    private Long reviewsNumber;
    private int duplicateCount;
    private String siteUrl;

}
