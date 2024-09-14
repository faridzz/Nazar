package org.example.nazar.service.scraper;

import org.example.nazar.model.Review;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;


public interface IReviewsScraper<T  > {

    List<Review> getReviews(URL url) throws IOException, InterruptedException, ExecutionException;

    URL getUrlOfProduct(T product);


}
