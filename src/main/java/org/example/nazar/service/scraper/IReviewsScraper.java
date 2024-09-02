package org.example.nazar.service.scraper;

import org.example.nazar.model.Review;


import java.io.IOException;
import java.net.URL;
import java.util.List;


public interface IReviewsScraper<T>  {

    List<Review> getReviews(URL url) throws IOException, InterruptedException;
    URL  getUrlOfProduct(T product);
}
