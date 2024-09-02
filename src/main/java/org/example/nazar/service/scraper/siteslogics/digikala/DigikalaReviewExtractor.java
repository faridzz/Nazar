package org.example.nazar.service.scraper.siteslogics.digikala;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.nazar.exception.EndOfReviewAPiException;
import org.example.nazar.model.Review;
import org.example.nazar.service.scraper.IReviewExtractor;
import org.example.nazar.util.time.datereformater.IDateReFormater;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;


@Component
public class DigikalaReviewExtractor implements IReviewExtractor {
    @Override
    public List<Review> extractReviews(Object doc, IDateReFormater dateReFormater) {
        List<Review> reviewList = new LinkedList<>();
        String response = (String) doc;
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(response);
            if (jsonNode.get("status").asInt() == 200) {
                JsonNode comments = jsonNode.get("data").get("comments");
                for (JsonNode comment : comments) {
                    List<Integer> jalaliDate = dateReFormater.dateSplitter(comment.get("created_at").asText());
                    LocalDate date = dateReFormater.reFormater(jalaliDate.get(0), jalaliDate.get(1), jalaliDate.get(2));
                    Review review = Review.builder()
                            .author(comment.get("user_name").asText())
                            .content(comment.get("body").asText())
                            .voteUp(comment.get("reactions").get("likes").asInt())
                            .voteDown(comment.get("reactions").get("dislikes").asInt())
                            .rating(comment.get("rate").asInt())
                            .maximumRate(5)
                            .postedAt(date)
                            .build();
                    if (review.getAuthor().isEmpty() || review.getContent().isEmpty()) continue;
                    reviewList.add(review);
                }
                return reviewList;
            } else {
                throw new EndOfReviewAPiException();
            }

        } catch (Exception e) {
            throw new EndOfReviewAPiException("error while extract digikala reviews");
        }

    }
}
