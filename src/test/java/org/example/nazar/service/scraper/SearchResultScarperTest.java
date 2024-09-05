package org.example.nazar.service.scraper;

import org.example.nazar.dto.BaseDTO;
import org.example.nazar.service.scraper.findbestmatch.IFindBestMatch;
import org.example.nazar.util.network.IHttpRequestSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchResultScarperTest {

    @Mock
    private IHttpRequestSender httpGetRequestSender;

    @Mock
    private IFindBestMatch<BaseDTO> findBestMatchLevenshteinDistance;

    @InjectMocks
    private ConcreteSearchResultScarper scraper;

    @BeforeEach
    void setUp() {
        scraper = spy(new ConcreteSearchResultScarper(httpGetRequestSender, findBestMatchLevenshteinDistance));
    }

    @Test
    void getSearchResults_Success() throws IOException, InterruptedException {
        // Arrange
        String productName = "mi 9";
        String type = "smartphone";
        String siteUrl = "https://mockurl.com/search?productName=" + productName + "&type=" + type;
        String responseBody = "title:mi 9 , code:3ejdjief and bla bla";
        List<BaseDTO> mockResults = new ArrayList<>();
        BaseDTO mockDTO = new BaseDTO();
        mockDTO.setTitle(productName);
        mockResults.add(mockDTO);

        doReturn(siteUrl).when(scraper).buildSearchUrl(productName, type);
        when(httpGetRequestSender.sendRequest(siteUrl)).thenReturn(responseBody);
        doReturn(mockResults).when(scraper).extractSearchResults(responseBody);

        // Act
        List<BaseDTO> results = scraper.getSearchResults(productName, type);

        // Assert
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getTitle()).isEqualTo(productName);
        verify(httpGetRequestSender, times(1)).sendRequest(siteUrl);
    }

    @Test
    void getSearchResults_Exception() throws IOException, InterruptedException {
        // Arrange
        String productName = "testProduct";
        String type = "testType";

        when(httpGetRequestSender.sendRequest(anyString())).thenThrow(new IOException("Request failed"));

        // Act
        List<BaseDTO> results = scraper.getSearchResults(productName, type);

        // Assert
        assertThat(results).isEmpty();
        verify(httpGetRequestSender, times(1)).sendRequest(anyString());
    }

    @Test
    void findBestResult_Found() {
        // Arrange
        BaseDTO result = new BaseDTO();
        result.setTitle("testProduct");
        List<BaseDTO> results = List.of(result);

        when(findBestMatchLevenshteinDistance.finder(anyList(), anyString())).thenReturn(Optional.of(result));

        // Act
        Optional<BaseDTO> bestResult = scraper.findBestResult(results);

        // Assert
        assertThat(bestResult).isPresent();
        assertThat(bestResult.get().getTitle()).isEqualTo("testProduct");
    }

    @Test
    void findBestResult_NotFound() {
        // Arrange
        List<BaseDTO> results = new ArrayList<>();

        // Act
        Optional<BaseDTO> bestResult = scraper.findBestResult(results);

        // Assert
        assertThat(bestResult).isEmpty();
    }

    // Concrete implementation of SearchResultScarper for testing purposes
    private static class ConcreteSearchResultScarper extends SearchResultScarper<BaseDTO> {

        protected ConcreteSearchResultScarper(IHttpRequestSender httpGetRequestSender,
                                              IFindBestMatch<BaseDTO> findBestMatchLevenshteinDistance) {
            super(httpGetRequestSender, findBestMatchLevenshteinDistance);
        }

        @Override
        protected String buildSearchUrl(String productName, String type) {
            return "https://mockurl.com/search?productName=" + productName + "&type=" + type;
        }

        @Override
        protected List<BaseDTO> extractSearchResults(String responseBody) {
            // Mock extraction logic for testing
            BaseDTO dto = new BaseDTO();
            dto.setTitle("mi 9");  // Assuming the extracted title
            return List.of(dto);
        }

        @Override
        protected BaseDTO createDTOInstance() {
            return new BaseDTO();
        }
    }
}
