package org.example.nazar.service.scraper;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.FullReviewDTO;

import org.example.nazar.dto.ReviewResultDTO;
import org.example.nazar.enums.SiteType;
import org.example.nazar.exception.DuplicateHashIdException;
import org.example.nazar.exception.NotFoundException;
import org.example.nazar.model.Product;
import org.example.nazar.model.ProductReview;
import org.example.nazar.model.Review;
import org.example.nazar.service.scraper.mainservices.DataBaseService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MultiThreadReviewAdder<T extends BaseDTO> implements IReviewAdder<T> {
    protected final DataBaseService dataBaseService;
    protected final IReviewsScraper<T> reviewsScraper;
    protected final ExecutorService executorService;
    protected Integer numberOfFixedThreadPool;

    public MultiThreadReviewAdder(DataBaseService dataBaseService, IReviewsScraper<T> reviewsScraper, Integer numberOfFixedThreadPool) {
        this.dataBaseService = dataBaseService;
        this.reviewsScraper = reviewsScraper;
        this.numberOfFixedThreadPool = numberOfFixedThreadPool > 0 ? numberOfFixedThreadPool : 60;
        this.executorService = Executors.newFixedThreadPool(this.numberOfFixedThreadPool);
    }


    protected ProductReview addReviewToDatabase(FullReviewDTO fullReviewDTO) {
        try {
            return dataBaseService.addReview(fullReviewDTO);
        } catch (DuplicateHashIdException e) {
            throw new DuplicateHashIdException();

        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("Error adding review", e);
            throw new RuntimeException(e); // Wrapping exception into a runtime exception to handle it in the caller method
        }
    }

    @Override
    public ReviewResultDTO addReviewsToDatabase(T baseDto, SiteType siteType, String typeName) throws IOException, InterruptedException, ExecutionException {
        AtomicInteger duplicateCount = new AtomicInteger(0);
        AtomicInteger throwableCount = new AtomicInteger(0);
        String siteUrl = siteType.getUrl();
        typeName = typeName.toLowerCase().trim();
        Product product = dataBaseService.addProduct((baseDto).getTitle(), typeName);
        dataBaseService.addSite(siteType);

        URL url = reviewsScraper.getUrlOfProduct(baseDto);

        List<Review> reviewList = new ArrayList<>(reviewsScraper.getReviews(url));

        FullReviewDTO.FullReviewDTOBuilder fullReviewDTOBuilder = FullReviewDTO.builder()
                .siteUrl(siteUrl)
                .productName(product.getName())
                .typeName(typeName);

        // Submitting tasks to the executor service
        List<CompletableFuture<ProductReview>> futures = reviewList.stream()
                .map(review -> CompletableFuture.supplyAsync(() -> addReviewToDatabase(fullReviewDTOBuilder.review(review).build()), executorService))
                .toList();

        futures.parallelStream()
                .forEach(future -> {
                    try {
                        future.get();
                    } catch (ExecutionException e) {
                        throwableCount.incrementAndGet();
                        Throwable cause = e.getCause();
                        if (cause instanceof DuplicateHashIdException) {
                            duplicateCount.incrementAndGet();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.error("Thread was interrupted", e);
                    }
                });

        return new ReviewResultDTO(siteUrl, (long) reviewList.size(), duplicateCount.get(), throwableCount.get());
    }
}
