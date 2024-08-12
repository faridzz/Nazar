package org.example.nazar.service.scraper.siteslogics.mobile;

import org.example.nazar.model.Review;
import org.example.nazar.service.scraper.IReviewExtractor;
import org.example.nazar.util.time.datereformater.IDateReFormater;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
class MobileReviewExtractor implements IReviewExtractor {
    @Override
    public List<Review> extractReviews(Document doc, IDateReFormater dateReFormater) {
        List<Review> reviews = new ArrayList<>();
        Elements commentElements = doc.select(".comment");
        for (Element commentElement : commentElements) {
            Element h3Tag = commentElement.selectFirst("h3");
            if (h3Tag != null) {
                Element strong = h3Tag.selectFirst("strong");
                Element span = h3Tag.selectFirst("span");
                Element b = span != null ? span.selectFirst("b") : null;
                Element paddTag = commentElement.selectFirst(".padd");
                Element voteDown = commentElement.selectFirst(".votedown");
                Element voteDownSpan = voteDown != null ? voteDown.selectFirst("span") : null;
                Element voteUp = commentElement.selectFirst(".voteup");
                Element voteUpSpan = voteUp != null ? voteUp.selectFirst("span") : null;

                if (paddTag != null && strong != null && b != null && voteDownSpan != null && voteUpSpan != null) {
                    String author = strong.text();
                    String content = paddTag.ownText();
                    String postedAtString = b.text();
                    int voteDownInt = Integer.parseInt(voteDownSpan.ownText());
                    int voteUpInt = Integer.parseInt(voteUpSpan.ownText());
                    int vote = voteUpInt - voteDownInt;
                    List<Integer> date = dateReFormater.dateSplitter(postedAtString);
                    LocalDate postedAtLocalDate = dateReFormater.reFormater(date.get(0), date.get(1), date.get(2));

                    Review review = new Review();
                    review.setAuthor(author);
                    review.setContent(content);
                    review.setPostedAt(postedAtLocalDate);
                    review.setVoteUp(voteUpInt);
                    review.setVoteDown(voteDownInt);
                    review.setRating(vote);

                    reviews.add(review);
                }
            }
        }
        return reviews;
    }


}