package org.example.nazar.service.scraper;

import org.example.nazar.model.Review;
import org.example.nazar.util.time.datereformater.IDateReFormater;
import org.jsoup.nodes.Document;

import java.util.List;

public interface IReviewExtractor<T, E> {
    List<Review> extractReviews(T doc, IDateReFormater dateReFormater);

    int findLastPageNumber(E data);
}
