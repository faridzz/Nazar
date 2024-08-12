package org.example.nazar.service.scraper;

import org.example.nazar.model.Review;


import java.net.URL;
import java.util.List;


public interface IReviewsScraper  {

    List<Review> getReviews(URL url);
}
