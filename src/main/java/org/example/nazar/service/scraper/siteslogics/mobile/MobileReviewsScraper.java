package org.example.nazar.service.scraper.siteslogics.mobile;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.MobileDTO;
import org.example.nazar.exception.ScraperException;
import org.example.nazar.model.Review;
import org.example.nazar.service.scraper.IPageFetcher;
import org.example.nazar.service.scraper.IReviewExtractor;
import org.example.nazar.service.scraper.IReviewsScraper;
import org.example.nazar.util.time.datereformater.IDateReFormater;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MobileReviewsScraper implements IReviewsScraper<MobileDTO> {
    private final IDateReFormater iDateReFormater;
    private final IPageFetcher pageFetcher;
    private final IReviewExtractor reviewExtractor;

    public MobileReviewsScraper(
            @Qualifier("jalaliToGregorianDateMobile") IDateReFormater iDateReFormater,
            IPageFetcher pageFetcher,
            @Qualifier("mobileReviewExtractor") IReviewExtractor reviewExtractor) {
        this.iDateReFormater = iDateReFormater;
        this.pageFetcher = pageFetcher;
        this.reviewExtractor = reviewExtractor;
    }

    @Override
    public URL getUrlOfProduct(MobileDTO product) {
        if (product.getUrl().isEmpty()) {
            throw new ScraperException("cant build url for " + product.getUrl());
        }
        String prefixURL = "https://www.mobile.ir";
        String url = prefixURL + product.getUrl();
        String newUrlString = url.replace("specifications", "comments");
        try {
            return new URL(newUrlString);
        } catch (MalformedURLException e) {
            throw new ScraperException("cant build url for " + url, e);
        }

    }

    @Override
    public List<Review> getReviews(URL url) {
        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Review> reviews = new ArrayList<>();
        Document doc;
        int pageNumber;

        try {
            doc = pageFetcher.fetchPage(url);
            pageNumber = getNumberOfPages(doc);
        } catch (IOException e) {
            throw new ScraperException(("Error connecting to " + url), e);
        }

        for (int firstPageNumber = 1; firstPageNumber <= pageNumber; firstPageNumber++) {
            int pageNum = firstPageNumber;
            executorService.submit(() -> {
                String pageUrl = url + "?page=" + pageNum;
                try {
                    Document pageDoc = pageFetcher.fetchPage(new URL(pageUrl));
                    List<Review> pageReviews = reviewExtractor.extractReviews(pageDoc, iDateReFormater);
                    synchronized (reviews) {
                        reviews.addAll(pageReviews);
                    }
                } catch (IOException e) {
                    log.error("Error connecting to {}", pageUrl, e);
                    throw new ScraperException(("Error connecting to " + pageUrl), e);
                }
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return reviews;
    }

    private Integer getNumberOfPages(Document doc) {
        Element userComment = doc.selectFirst(".user-comments");
        if (userComment != null && userComment.selectFirst(".comment") != null) {
            Element pagination = userComment.selectFirst(".pagination");
            if (pagination != null) {
                Elements paginationElements = pagination.select("a");
                int numOfaTags = paginationElements.size();
                Element aTagWithNumOfPages = paginationElements.get(numOfaTags - 2);
                return Integer.parseInt(aTagWithNumOfPages.text());
            }
        }
        return 1;
    }
}
