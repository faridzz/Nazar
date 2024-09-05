package org.example.nazar.service.scraper.siteslogics.digikala;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.nazar.exception.EndOfReviewAPiException;
import org.example.nazar.model.Review;
import org.example.nazar.service.scraper.IReviewExtractor;
import org.example.nazar.util.time.datereformater.IDateReFormater;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DigikalaReviewExtractorTest {

    @InjectMocks
    private DigikalaReviewExtractor reviewExtractor;

    @Mock
    private IDateReFormater dateReFormater;

    @Test
    void testExtractReviews_Success() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock data
        String jsonResponse = "{"
                + "\"status\": 200,"
                + "\"data\": {"
                + "\"comments\": ["
                + "{"
                + "\"user_name\": \"علی مسعودی وشکی\","
                + "\"body\": \"راضیم\","
                + "\"reactions\": {\"likes\": 2, \"dislikes\": 1},"
                + "\"rate\": 5,"
                + "\"created_at\": \"5 شهریور 1403\""
                + "}"
                + "]"
                + "}"
                + "}";

        // Mock behavior of dateReFormater
        when(dateReFormater.dateSplitter("5 شهریور 1403")).thenReturn(Arrays.asList(1403, 6, 5));
        when(dateReFormater.reFormater(1403, 6, 5)).thenReturn(LocalDate.of(2024, 8, 26)); // Adjust the date as needed

        // Call the method under test
        List<Review> reviews = reviewExtractor.extractReviews(jsonResponse, dateReFormater);

        // Verify the result
        assertNotNull(reviews);
        assertEquals(1, reviews.size());

        Review review = reviews.get(0);
        assertEquals("علی مسعودی وشکی", review.getAuthor());
        assertEquals("راضیم", review.getContent());
        assertEquals(2, review.getVoteUp());
        assertEquals(1, review.getVoteDown());
        assertEquals(5, review.getRating());
        assertEquals(5, review.getMaximumRate());
        assertEquals(LocalDate.of(2024, 8, 26), review.getPostedAt());
    }

    @Test
    void testExtractReviews_Failure() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock data
        String jsonResponse = "{"
                + "\"status\": 500,"
                + "\"data\": {}"
                + "}";

        // Call the method under test and assert exception
        assertThrows(EndOfReviewAPiException.class, () -> {
            reviewExtractor.extractReviews(jsonResponse, dateReFormater);
        });
    }

    @Test
    void testExtractReviews_ParsingError() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock data with invalid JSON
        String jsonResponse = "{invalid json}";

        // Call the method under test and assert runtime exception
        assertThrows(RuntimeException.class, () -> {
            reviewExtractor.extractReviews(jsonResponse, dateReFormater);
        });
    }
}
