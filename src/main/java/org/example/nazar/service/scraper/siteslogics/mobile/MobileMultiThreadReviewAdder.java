package org.example.nazar.service.scraper.siteslogics.mobile;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.FullReviewDTO;
import org.example.nazar.dto.MobileDTO;
import org.example.nazar.dto.ReviewResultDTO;
import org.example.nazar.exception.DuplicateHashIdException;
import org.example.nazar.model.Product;
import org.example.nazar.model.ProductReview;
import org.example.nazar.model.Review;
import org.example.nazar.service.scraper.IFindUrlOfReviews;
import org.example.nazar.service.scraper.IReviewAdder;
import org.example.nazar.service.scraper.IReviewsScraper;
import org.example.nazar.service.scraper.mainservices.DataBaseService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class MobileMultiThreadReviewAdder implements IReviewAdder {
    private final DataBaseService dataBaseService;
    private final IReviewsScraper mobileReviewsScraper;
    private final IFindUrlOfReviews mobileFindUrlOfReviews;

    public MobileMultiThreadReviewAdder(DataBaseService dataBaseService, IReviewsScraper mobileReviewsScraper, IFindUrlOfReviews mobileFindUrlOfReviews) {
        this.dataBaseService = dataBaseService;
        this.mobileReviewsScraper = mobileReviewsScraper;
        this.mobileFindUrlOfReviews = mobileFindUrlOfReviews;
    }

    @Override
    public ReviewResultDTO addReviews(BaseDTO baseDto, String siteUrl, String typeName) {
        List<ProductReview> productReviews = new LinkedList<>();
        MobileDTO mobileDTO = (MobileDTO) baseDto;
        AtomicInteger duplicateCount = new AtomicInteger(0);

        siteUrl = siteUrl.toLowerCase().trim();
        typeName = typeName.toLowerCase().trim();
        Product product = dataBaseService.addProduct(mobileDTO.getTitle(), typeName);

        List<Review> reviewList = new ArrayList<>(mobileReviewsScraper.getReviews(mobileFindUrlOfReviews.getUrlOfProduct(mobileDTO).get()));

        FullReviewDTO.FullReviewDTOBuilder fullReviewDTOBuilder = FullReviewDTO.builder().siteUrl(siteUrl).productName(product.getName());
        List<CompletableFuture<ProductReview>> futures = reviewList.stream().
                map(review -> addReviewAsync(fullReviewDTOBuilder.review(review).build())).
                toList();

        futures.parallelStream()
                .forEach(future -> {
                    try {
                        future.get();
                    } catch (ExecutionException e) {
                        Throwable cause = e.getCause();
                        if (cause instanceof DuplicateHashIdException) {
                            // مدیریت خطای DuplicateHashIdException
                            duplicateCount.incrementAndGet();
                        } else {
                            // مدیریت دیگر خطاها
                            log.error("Unexpected error", cause);
                        }
                    } catch (InterruptedException e) {
                        // مدیریت خطای InterruptedException
                        log.error("Thread was interrupted", e);
                    }
                });


        return new ReviewResultDTO((long) reviewList.size(), duplicateCount.get(), siteUrl);

    }

    /**
     * افزودن نظر به صورت غیرهمزمان.
     * این روش به صورت غیرهمزمان یک نظر را به محصول اضافه می‌کند و در صورت تکراری بودن هش، خطا را مدیریت می‌کند.
     *
     * @param fullReviewDTO اطلاعات کاملی از نظز
     * @return یک شیء CompletableFuture که نتیجه افزودن نظر را برمی‌گرداند
     */
    @Async("addReviewExecutor")
    public CompletableFuture<ProductReview> addReviewAsync(FullReviewDTO fullReviewDTO) {
        try {
            ProductReview productReview = dataBaseService.addReview(fullReviewDTO);
            return CompletableFuture.completedFuture(productReview);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

}
