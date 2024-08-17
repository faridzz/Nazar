package org.example.nazar.service.scraper.mainservices;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.MobileDTO;
import org.example.nazar.dto.ReviewResultDTO;
import org.example.nazar.exception.DuplicateHashIdException;
import org.example.nazar.model.*;
import org.example.nazar.service.scraper.IAddReviewToDataBase;
import org.example.nazar.service.scraper.IFindUrlOfReviews;
import org.example.nazar.service.scraper.IReviewsScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Service
public class MobileService implements IAddReviewToDataBase {

    private final DataBaseService dataBaseService;
    private final IReviewsScraper mobileReviewsScraper;
    private final IFindUrlOfReviews mobileFindUrlOfReviews;

    @Autowired
    public MobileService(DataBaseService dataBaseService, IReviewsScraper mobileReviewsScraper, IFindUrlOfReviews mobileFindUrlOfReviews) {
        this.dataBaseService = dataBaseService;
        this.mobileReviewsScraper = mobileReviewsScraper;
        this.mobileFindUrlOfReviews = mobileFindUrlOfReviews;
    }

    /**
     * افزودن نظرات به صورت چند نخی.
     * این روش نظرات را به صورت چند نخی اضافه می‌کند و قابلیت تشخیص و حذف نظرات تکراری را دارد.
     * این روش کندتر از روش دیگر است.
     *
     * @param baseDto  اطلاعات پایه محصول
     * @param siteUrl  آدرس سایت
     * @param typeName نوع محصول
     * @return لیستی از نظرات محصول
     */
    @Override
    public ReviewResultDTO addReviewOneByOne(BaseDTO baseDto, String siteUrl, String typeName) {
        List<ProductReview> productReviews = new LinkedList<>();
        MobileDTO mobileDTO = (MobileDTO) baseDto;
        AtomicInteger duplicateCount = new AtomicInteger(0);

        siteUrl = siteUrl.toLowerCase().trim();
        typeName = typeName.toLowerCase().trim();
        Product product = dataBaseService.addProduct(mobileDTO.getTitle(), typeName);

        List<Review> reviewList = new ArrayList<>(mobileReviewsScraper.getReviews(mobileFindUrlOfReviews.getUrlOfProduct(mobileDTO).get()));

        String finalSiteUrl = siteUrl;
        List<CompletableFuture<ProductReview>> futures = reviewList.stream().
                map(review -> addReviewAsync(review, product.getName(), finalSiteUrl)).
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

        return new ReviewResultDTO((long) reviewList.size(), duplicateCount.get());

    }

    /**
     * افزودن نظر به صورت غیرهمزمان.
     * این روش به صورت غیرهمزمان یک نظر را به محصول اضافه می‌کند و در صورت تکراری بودن هش، خطا را مدیریت می‌کند.
     *
     * @param review      نظر
     * @param productName نام محصول
     * @param siteUrl     آدرس سایت
     * @return یک شیء CompletableFuture که نتیجه افزودن نظر را برمی‌گرداند
     */
    @Async("addReviewExecutor")
    public CompletableFuture<ProductReview> addReviewAsync(Review review, String productName, String siteUrl) {
        try {
            ProductReview productReview = dataBaseService.addReview(review, productName, siteUrl);
            return CompletableFuture.completedFuture(productReview);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * افزودن نظرات به صورت تک نخی.
     * این روش نظرات را به صورت تک نخی اضافه می‌کند و قابلیت تشخیص و حذف نظرات تکراری را ندارد.
     * این روش سریعتر از روش چند نخی است.
     *
     * @param baseDto  اطلاعات پایه محصول
     * @param siteUrl  آدرس سایت
     * @param typeName نوع محصول
     * @return لیستی از نظرات محصول
     */
    @Override
    public ReviewResultDTO addReviewAll(BaseDTO baseDto, String siteUrl, String typeName) {
        List<ProductReview> productReviews = new LinkedList<>();
        MobileDTO mobileDTO = (MobileDTO) baseDto;

        siteUrl = siteUrl.toLowerCase().trim();
        typeName = typeName.toLowerCase().trim();
        Product product = dataBaseService.addProduct(mobileDTO.getTitle(), typeName);

        List<Review> reviewList = mobileReviewsScraper.getReviews(mobileFindUrlOfReviews.getUrlOfProduct(mobileDTO).get());
        Long reviewNumber = (long) dataBaseService.addReviews(reviewList, product.getName(), siteUrl).size();
        return new ReviewResultDTO(reviewNumber, 0);
    }

}
