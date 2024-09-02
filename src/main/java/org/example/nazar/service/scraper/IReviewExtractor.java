package org.example.nazar.service.scraper;

import org.example.nazar.model.Review;
import org.example.nazar.util.time.datereformater.IDateReFormater;
import org.jsoup.nodes.Document;

import java.util.List;

public interface IReviewExtractor {
    List<Review> extractReviews(Object doc, IDateReFormater dateReFormater);
}
