package org.example.nazar.service.scraper;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.BaseDTO;
import org.example.nazar.service.scraper.findbestmatch.IFindBestMatch;
import org.example.nazar.util.network.IHttpRequestSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class SearchResultScarper<T extends BaseDTO> implements ISearchResultScarper<T> {
    private final IHttpRequestSender httpGetRequestSender;
    private final IFindBestMatch<T> findBestMatchLevenshteinDistance;

    protected SearchResultScarper(IHttpRequestSender httpGetRequestSender,
                                  IFindBestMatch<T> findBestMatchLevenshteinDistance) {
        this.httpGetRequestSender = httpGetRequestSender;
        this.findBestMatchLevenshteinDistance = findBestMatchLevenshteinDistance;
    }

    // Abstract method for building the search URL (must be implemented in subclasses)
    protected abstract String buildSearchUrl(String productName, String type);

    // Abstract method for extracting search results (must be implemented in subclasses)
    protected abstract List<T> extractSearchResults(String responseBody);

    // Method for processing search results and converting them into a list of DTOs
    @Override
    public List<T> getSearchResults(String productName, String type) {
        String searchUrl = buildSearchUrl(productName, type);
        List<T> searchResults;
        String responseBody;

        try {
            responseBody = httpGetRequestSender.sendRequest(searchUrl);
        } catch (IOException | InterruptedException e) {
            log.error("Error while sending request", e);
            // Re-interrupt the current thread in case of an InterruptedException
            Thread.currentThread().interrupt();
            return new ArrayList<>();
        }

        searchResults = extractSearchResults(responseBody);

        if (searchResults.isEmpty()) {
            log.error("No search results found");
            return new ArrayList<>();
        }

        //add searcher word to the list of result(last one)
        T searchedInput = createDTOInstance();
        searchedInput.setTitle(productName);
        searchResults.add(searchedInput);

        // Convert List<T> to List<BaseDTO> using a proper method
        return new ArrayList<>(searchResults);
    }

    // Abstract method for creating an instance of T (must be implemented in subclasses)
    protected abstract T createDTOInstance();

    @Override
    public Optional<T> findBestResult(List<T> results) {
        if (results.isEmpty()) {
            log.error("No results found");
            return Optional.empty();
        } else {
            String searchedString = results.get(results.size() - 1).getTitle();
            return findBestMatchLevenshteinDistance.finder(results, searchedString);
        }
    }
}
