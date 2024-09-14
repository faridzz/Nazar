package org.example.nazar.service.scraper;

import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.ReviewResultDTO;
import org.example.nazar.enums.SiteType;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface IReviewAdder<T extends BaseDTO> {


    ReviewResultDTO addReviewsToDatabase(T baseDto, SiteType siteType, String typeName) throws IOException, InterruptedException, ExecutionException;

}
