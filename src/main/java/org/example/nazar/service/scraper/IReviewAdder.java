package org.example.nazar.service.scraper;

import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.ReviewResultDTO;

import java.io.IOException;

public interface IReviewAdder<T extends BaseDTO> {


    ReviewResultDTO addReviewsToDatabase(T baseDto, String siteUrl, String typeName) throws IOException, InterruptedException;

}
