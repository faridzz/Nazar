package org.example.nazar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class ReviewResultDTO {

    public ReviewResultDTO(String siteUrl, Long reviewsNumber, int duplicateCount) {
        this.siteUrl = siteUrl;
        this.reviewsNumber = reviewsNumber;
        this.duplicateCount = duplicateCount;
    }

    private String siteUrl;
    private Long reviewsNumber;
    private int duplicateCount;
    private int throwableCount;

    @Override
    public String toString() {
        return "ReviewResultDTO{" +
                "siteUrl='" + siteUrl + '\'' +
                ", reviewsNumber=" + reviewsNumber +
                ", throwableCount=" + throwableCount +
                ", duplicateCount=" + duplicateCount +
                ", reviews added secusecfully =" + (reviewsNumber - throwableCount) +
                '}';
    }
}

