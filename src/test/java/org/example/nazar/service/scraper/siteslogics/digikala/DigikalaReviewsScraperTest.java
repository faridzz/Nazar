package org.example.nazar.service.scraper.siteslogics.digikala;

import org.example.nazar.dto.DigikalaDTO;
import org.example.nazar.model.Review;
import org.example.nazar.service.scraper.IReviewExtractor;
import org.example.nazar.util.network.IHttpRequestSender;
import org.example.nazar.util.time.datereformater.IDateReFormater;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DigikalaReviewsScraperTest {
    @Mock
    IHttpRequestSender get;
    @Mock
    IReviewExtractor extractor;
    @Mock
    IDateReFormater dateReFormater;

    @InjectMocks
    DigikalaReviewsScraper digikalaReviewsScraper;

    @Test
    void getUrlOfProduct() throws MalformedURLException {
        // Arrange
        DigikalaDTO dto = new DigikalaDTO();
        dto.setUrl("/product/dkp-14411355/گوشی-موبایل-شیائومی-مدل-poco-x6-5g-دو-سیم-کارت-ظرفیت-256-گیگابایت-و-رم-12-گیگابایت-clone-1-of-14192208");
        URL expectedUrl = new URL("https://api.digikala.com/v1/rate-review/products/14411355");

        // Act
        URL url = digikalaReviewsScraper.getUrlOfProduct(dto);

        // Assert
        assertThat(url).as("check url to equal with %s", expectedUrl).isEqualTo(expectedUrl);
    }

    @Test
    void getReviews_ValidResponse() throws IOException, InterruptedException {
        // Arrange
        DigikalaDTO dto = new DigikalaDTO();
        dto.setUrl("/product/dkp-14411355/گوشی-موبایل-شیائومی-مدل-poco-x6-5g-دو-سیم-کارت-ظرفیت-256-گیگابایت-و-رم-12-گیگابایت-clone-1-of-14192208");
        URL url = new URL("https://api.digikala.com/v1/rate-review/products/14411355");

        String responsePage1 = "{ \"status\": 200, \"data\": { \"comments\": [ { \"user_name\": \"Test User\", \"body\": \"Great product!\", \"reactions\": { \"likes\": 10, \"dislikes\": 1 }, \"rate\": 4, \"created_at\": \"6 شهریور 1403\" } ] } }";
        String responsePage2 = "{ \"status\": 200, \"data\": { \"comments\": [] } }"; // Empty page response

        when(get.sendRequest(url + "/?page=1")).thenReturn(responsePage1);
        when(get.sendRequest(url + "/?page=2")).thenReturn(responsePage2);

        List<Review> expectedReviews = new LinkedList<>();
        expectedReviews.add(Review.builder()
                .author("Test User")
                .content("Great product!")
                .voteUp(10)
                .voteDown(1)
                .rating(4)
                .maximumRate(5)
                .postedAt(LocalDate.of(2024, 8, 27)) // Adjust according to your date conversion
                .build());


        when(extractor.extractReviews(responsePage1, dateReFormater)).thenReturn(expectedReviews);
        when(extractor.extractReviews(responsePage2, dateReFormater)).thenReturn(List.of());
        // Act
        List<Review> actualReviews = digikalaReviewsScraper.getReviews(url);

        // Assert
        assertThat(actualReviews).isEqualTo(expectedReviews);
    }

    @Test
    void getReviews_ThrowsException() throws IOException, InterruptedException {
        // Arrange
        DigikalaDTO dto = new DigikalaDTO();
        dto.setUrl("/product/dkp-14411355/گوشی-موبایل-شیائومی-مدل-poco-x6-5g-دو-سیم-کارت-ظرفیت-256-گیگابایت-و-رم-12-گیگابایت-clone-1-of-14192208");
        URL url = new URL("https://api.digikala.com/v1/rate-review/products/14411355");

        when(get.sendRequest(url + "/?page=1")).thenThrow(new IOException("Network error"));

        // Act & Assert
        assertThrows(IOException.class, () -> {
            digikalaReviewsScraper.getReviews(url);
        });
    }
}
