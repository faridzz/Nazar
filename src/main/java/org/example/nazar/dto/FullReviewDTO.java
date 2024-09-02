package org.example.nazar.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.nazar.model.Review;

@Getter
@Setter
@Builder
public class FullReviewDTO extends BaseDTO {
    Review review;
    String productName;
    String siteUrl;
    String typeName;
}
