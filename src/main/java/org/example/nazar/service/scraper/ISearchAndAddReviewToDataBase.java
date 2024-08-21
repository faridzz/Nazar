package org.example.nazar.service.scraper;

import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.ReviewResultDTO;
import org.example.nazar.exception.DuplicateHashIdException;


public interface ISearchAndAddReviewToDataBase {


    ReviewResultDTO addReviewOneByOne(BaseDTO baseDto, String siteUrl, String typeName) throws DuplicateHashIdException;

    ReviewResultDTO addReviewAll(BaseDTO baseDto, String siteUrl, String typeName);

}
