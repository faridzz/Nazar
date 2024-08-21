package org.example.nazar.util.stringbuilder;

import org.example.nazar.dto.ReviewResultDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class BuildStringForController {
    public static String buildResultString(List<ReviewResultDTO> reviewResultDTOS, String productNameSearchInput, String productNameSearchResult) {
        StringBuilder resultBuilder = new StringBuilder();

        reviewResultDTOS.forEach(results -> resultBuilder.append(" | ")
                .append(results.getSiteUrl())
                .append("  Searched: ")
                .append(productNameSearchInput)
                .append(" Search results: ")
                .append(productNameSearchResult)
                .append(" , Number of reviews founded: ")
                .append(results.getReviewsNumber())
                .append(" , duplicated reviews: ")
                .append(results.getDuplicateCount())
                .append(" | "));

        return resultBuilder.toString();
    }

    public static ResponseEntity<String> buildErrorResponse(String productNameSearchInput, String productNameSearchResult, Exception e) {
        String errorMessage = "Searched: " + productNameSearchInput +
                " Search results: " + productNameSearchResult +
                " Error adding review: " + e.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}
