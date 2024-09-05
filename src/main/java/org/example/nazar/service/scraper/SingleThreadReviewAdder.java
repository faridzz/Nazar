package org.example.nazar.service.scraper;

import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.ReviewResultDTO;
import org.example.nazar.model.Product;
import org.example.nazar.model.Review;
import org.example.nazar.service.scraper.mainservices.DataBaseService;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class SingleThreadReviewAdder<T extends BaseDTO> implements IReviewAdder<T> {
    protected final DataBaseService dataBaseService;
    protected final IReviewsScraper<T> reviewsScraper;

    public SingleThreadReviewAdder(DataBaseService dataBaseService, IReviewsScraper<T> reviewsScraper) {
        this.dataBaseService = dataBaseService;
        this.reviewsScraper = reviewsScraper;
    }

    @Override
    public ReviewResultDTO addReviewsToDatabase(T baseDto, String siteUrl, String typeName) throws IOException, InterruptedException {
        siteUrl = siteUrl.toLowerCase().trim();
        typeName = typeName.toLowerCase().trim();
        Product product = dataBaseService.addProduct(baseDto.getTitle(), typeName);
        URL url = reviewsScraper.getUrlOfProduct(baseDto);
        List<Review> reviewList = reviewsScraper.getReviews(url);
        Long reviewNumber = (long) dataBaseService.addReviews(reviewList, product.getName(), siteUrl).size();
        return new ReviewResultDTO(reviewNumber, 0, siteUrl);
    }
}
