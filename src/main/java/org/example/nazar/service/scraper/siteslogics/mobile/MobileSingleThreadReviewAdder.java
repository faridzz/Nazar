package org.example.nazar.service.scraper.siteslogics.mobile;


import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.MobileDTO;
import org.example.nazar.dto.ReviewResultDTO;
import org.example.nazar.model.Product;
import org.example.nazar.model.Review;
import org.example.nazar.service.scraper.IFindUrlOfReviews;
import org.example.nazar.service.scraper.IReviewAdder;
import org.example.nazar.service.scraper.IReviewsScraper;
import org.example.nazar.service.scraper.mainservices.DataBaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileSingleThreadReviewAdder implements IReviewAdder {
    private final DataBaseService dataBaseService;
    private final IReviewsScraper mobileReviewsScraper;
    private final IFindUrlOfReviews mobileFindUrlOfReviews;

    public MobileSingleThreadReviewAdder(DataBaseService dataBaseService, IReviewsScraper mobileReviewsScraper, IFindUrlOfReviews mobileFindUrlOfReviews) {
        this.dataBaseService = dataBaseService;
        this.mobileReviewsScraper = mobileReviewsScraper;
        this.mobileFindUrlOfReviews = mobileFindUrlOfReviews;
    }


    @Override
    public ReviewResultDTO addReviews(BaseDTO baseDto, String siteUrl, String typeName) {
        MobileDTO mobileDTO = (MobileDTO) baseDto;

        siteUrl = siteUrl.toLowerCase().trim();
        typeName = typeName.toLowerCase().trim();
        Product product = dataBaseService.addProduct(mobileDTO.getTitle(), typeName);

        List<Review> reviewList = mobileReviewsScraper.getReviews(mobileFindUrlOfReviews.getUrlOfProduct(mobileDTO).get());
        Long reviewNumber = (long) dataBaseService.addReviews(reviewList, product.getName(), siteUrl).size();
        return new ReviewResultDTO(reviewNumber, 0, siteUrl);
    }
}