package org.example.nazar.service.scraper;

import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.ReviewResultDTO;

public interface IReviewAdder {

    ReviewResultDTO addReviews(BaseDTO baseDto, String siteUrl, String typeName);

}
