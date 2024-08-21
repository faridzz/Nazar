package org.example.nazar.repository;

import org.example.nazar.model.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the ReviewRepository class.
 */
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    private Review review;

    @BeforeEach
    void setUp() {
        review = new Review();
        review.setAuthor("author");
        review.setContent("content");
        review.setVoteDown(3);
        review.setVoteUp(2);
        review.setRating(-1);
        review.setPostedAt(LocalDate.MAX);
        review.createHashId();
    }

    @Test
    void testExistsByHashId_Return_Review() {
        // Arrange
        reviewRepository.save(review);

        // Act
        boolean result = reviewRepository.existsByHashId(review.getHashId());

        // Assert
        assertThat(result)
                .as("Review with hashId '%s' should exist", review.getHashId())
                .isTrue();
    }

    @Test
    void testExistsByHashId_Return_False_If_Not_Exists() {
        String nonExistingHashId = "non-existing-hashId";

        // Act
        boolean result = reviewRepository.existsByHashId(nonExistingHashId);

        // Assert
        assertThat(result)
                .as("Review with hashId '%s' should not exist", nonExistingHashId)
                .isFalse();
    }

    @Test
    void testExistsByHashId_With_Null_HashId() {
        // Act
        boolean result = reviewRepository.existsByHashId(null);

        // Assert
        assertThat(result)
                .as("Review with null hashId should not exist")
                .isFalse();
    }

    @Test
    void testExistsByHashId_After_Delete() {
        // Arrange
        reviewRepository.save(review);

        // Act
        reviewRepository.delete(review);

        // Assert
        boolean result = reviewRepository.existsByHashId(review.getHashId());
        assertThat(result)
                .as("Review with hashId '%s' should not exist after deletion", review.getHashId())
                .isFalse();
    }
}
