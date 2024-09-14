package org.example.nazar.service.scraper;

import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.ReviewResultDTO;
import org.example.nazar.enums.SiteType;
import org.example.nazar.model.Product;
import org.example.nazar.model.Review;
import org.example.nazar.service.scraper.mainservices.DataBaseService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SingleThreadReviewAdder<T extends BaseDTO> implements IReviewAdder<T> {
    protected final DataBaseService dataBaseService;
    protected final IReviewsScraper<T> reviewsScraper;

    public SingleThreadReviewAdder(DataBaseService dataBaseService, IReviewsScraper<T> reviewsScraper) {
        this.dataBaseService = dataBaseService;
        this.reviewsScraper = reviewsScraper;
    }

    @Override
    public ReviewResultDTO addReviewsToDatabase(T baseDto, SiteType siteType, String typeName) throws IOException, InterruptedException, ExecutionException {
        String siteUrl = siteType.getUrl();
        typeName = typeName.toLowerCase().trim();
        Product product = dataBaseService.addProduct(baseDto.getTitle(), typeName);
        dataBaseService.addSite(siteType);
        URL url = reviewsScraper.getUrlOfProduct(baseDto);
        List<Review> reviewList = reviewsScraper.getReviews(url);
        Long reviewNumber = (long) dataBaseService.addReviews(reviewList, product.getName(), siteUrl).size();
        return new ReviewResultDTO(siteUrl, reviewNumber, 0);
    }
}
