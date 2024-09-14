package org.example.nazar.service.scraper.siteslogics.digikala;

import lombok.extern.slf4j.Slf4j;

import org.example.nazar.dto.DigikalaDTO;
import org.example.nazar.exception.EndOfReviewAPiException;
import org.example.nazar.exception.ScraperException;
import org.example.nazar.model.Review;
import org.example.nazar.service.scraper.IReviewExtractor;
import org.example.nazar.service.scraper.IReviewsScraper;

import org.example.nazar.util.network.IHttpRequestSender;
import org.example.nazar.util.time.datereformater.IDateReFormater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class DigikalaReviewsScraper implements IReviewsScraper<DigikalaDTO> {
    private final IHttpRequestSender get;
    private final IReviewExtractor<String, String> extractor;
    private final IDateReFormater dateReFormater;

    public DigikalaReviewsScraper(IHttpRequestSender get, @Qualifier("digikalaReviewExtractor") IReviewExtractor<String, String> extractor, @Qualifier("jalaliStringToGregorianDateDigikala") IDateReFormater dateReFormater) {
        this.get = get;
        this.extractor = extractor;
        this.dateReFormater = dateReFormater;
    }

//        https://api.digikala.com/v1/rate-review/products/14116027/?page=3

    @Override
    public URL getUrlOfProduct(DigikalaDTO product) {


        String prefixURL = "https://api.digikala.com/v1/rate-review/products/";
        String productHashId = extractIds(product);
        String stringUrl = prefixURL + productHashId;
        try {
            return new URL(stringUrl);
        } catch (MalformedURLException e) {
            throw new ScraperException("cnt build url from " + stringUrl + e + " error : " + e);
        }
    }


    @Override
    public List<Review> getReviews(URL url) throws IOException, InterruptedException, ExecutionException {
        ExecutorService ex = Executors.newFixedThreadPool(60);
        int lastPageNum = getResponseOfFirstPage(url);
        List<CompletableFuture<List<Review>>> completableFutures = new ArrayList<>();

        // اجرای هم‌زمان درخواست‌ها برای هر صفحه
        for (int num = 1; num <= lastPageNum; num++) {
            int finalNum = num;
            CompletableFuture<List<Review>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return reviewExtractor(finalNum, url.toString());
                } catch (IOException | InterruptedException e) {
                    log.error("error while get response from : {}{}", finalNum, url, e);
                    Thread.currentThread().interrupt();
                    return new ArrayList<>();
                }
            }, ex);
            completableFutures.add(future);
        }

        // استفاده از allOf برای جمع‌آوری همه CompletableFuture ها و تنظیم تایم‌اوت
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]))
                .orTimeout(50, TimeUnit.SECONDS);

        // جمع‌آوری نتایج پس از اتمام همه CompletableFuture ها یا در صورت تایم‌اوت
        CompletableFuture<List<Review>> allReviewsFuture = allFutures
                .thenApply(v -> {
                    List<Review> allReviews = new LinkedList<>();
                    for (CompletableFuture<List<Review>> future : completableFutures) {
                        try {
                            allReviews.addAll(future.get()); // ادغام نتایج
                        } catch (InterruptedException | ExecutionException e) {
                            Thread.currentThread().interrupt();
                            log.error("error while add or get review list");
                        }
                    }
                    return allReviews;
                })
                .exceptionally(e -> {
                    log.error("Error occurred while fetching reviews or timeout reached", e);
                    return new LinkedList<>(); // در صورت وقوع خطا یا تایم‌اوت، لیست خالی برمی‌گردد
                });

        List<Review> allReviews = allReviewsFuture.get(); // دریافت نتایج نهایی
        ex.shutdown(); // بستن ExecutorService بعد از اتمام کار
        return allReviews;
    }


    private List<Review> reviewExtractor(int pageNumber, String url) throws IOException, InterruptedException {
        String urlString = url + "/?page=" + pageNumber;
        String response = get.sendRequest(urlString);
        try {
            List<Review> reviewList = extractor.extractReviews(response, dateReFormater);
            if (reviewList.isEmpty()) {
                return new ArrayList<>();
            }
            return reviewList;
        } catch (EndOfReviewAPiException e) {
            return new ArrayList<>();
        }
    }

    private static String extractIds(DigikalaDTO dto) {
        Pattern idPattern = Pattern.compile("dkp-(\\d+)/");
        Matcher matcher = idPattern.matcher(dto.getUrl());
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new ScraperException("cnt find any id in this digikala product product data : " + dto);
        }
    }

    private int getResponseOfFirstPage(URL url) throws IOException, InterruptedException {
        String urlString = url + "/?page=" + 1;
        String response = get.sendRequest(urlString);
        return extractor.findLastPageNumber(response);
    }


}
