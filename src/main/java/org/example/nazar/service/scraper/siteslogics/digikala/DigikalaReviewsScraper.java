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
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class DigikalaReviewsScraper implements IReviewsScraper<DigikalaDTO> {
    private final IHttpRequestSender get;
    private final IReviewExtractor extractor;
    private final IDateReFormater dateReFormater;

    public DigikalaReviewsScraper(IHttpRequestSender get, @Qualifier("digikalaReviewExtractor") IReviewExtractor extractor, @Qualifier("jalaliStringToGregorianDateDigikala") IDateReFormater dateReFormater) {
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
    public List<Review> getReviews(URL url) throws IOException, InterruptedException {
        LinkedList<Review> reviewLinkedList = new LinkedList<>();
        int count = 1;
        while (true) {
            String urlString = url + "/?page=" + count;
            String response = get.sendRequest(urlString);
            try {
                List<Review> reviewList = extractor.extractReviews(response, dateReFormater);
                if (reviewList.isEmpty()) break;
                reviewLinkedList.addAll(reviewList);
                count += 1;
            } catch (EndOfReviewAPiException e) {
                break;
            }
        }
        return reviewLinkedList;
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
}
