package org.example.nazar.service.scraper;

import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.ReviewResultDto;
import org.example.nazar.exception.DuplicateHashIdException;

public interface IAddReviewToDataBase {


    ReviewResultDto addReviewOneByOne(BaseDTO baseDto, String siteUrl, String typeName) throws DuplicateHashIdException;

    ReviewResultDto addReviewAll(BaseDTO baseDto, String siteUrl, String typeName);

}
