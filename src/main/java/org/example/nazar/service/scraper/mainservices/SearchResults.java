package org.example.nazar.service.scraper.mainservices;

import org.example.nazar.dto.*;
import org.example.nazar.enums.SiteType;
import org.example.nazar.service.scraper.ISearchResultScarper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class SearchResults {

    private final ISearchResultScarper<MobileDTO> mobileSearchResultScarper;
    private final ISearchResultScarper<DigikalaDTO> digikalaSearchResultScarper;

    public SearchResults(
            @Qualifier("mobileSearchResultScarper") ISearchResultScarper<MobileDTO> mobileSearchResultScarper,
            @Qualifier("digikalaSearchResultScarper") ISearchResultScarper<DigikalaDTO> digikalaSearchResultScarper) {
        this.mobileSearchResultScarper = mobileSearchResultScarper;
        this.digikalaSearchResultScarper = digikalaSearchResultScarper;
    }

    // extract list of site who user want to search
    private static List<SiteType> extractSiteList(SearchSomethingDTO search) {
        return search.getSitesUrl().stream()
                .map(url -> url.toLowerCase().trim())
                .map(SiteType::getSiteType)
                .filter(siteType -> siteType != null)
                .toList();
    }

    // build a response to shows the user as part of search result
    private <T extends BaseDTO> SearchResponseDTO buildSearchResponse(ISearchResultScarper<T> searchResultScarper, SiteType siteType, String productName, String productType) {
        List<T> searchResults = searchResultScarper.getSearchResults(productName, productType);
        HashMap<String, String> resultHashMap = new HashMap<>();
        searchResults.forEach(product -> resultHashMap.put(product.getTitle(), product.getUrl()));


        SearchResponseDTO searchResponseDTO = new SearchResponseDTO();
        searchResponseDTO.setSiteType(siteType);
        searchResponseDTO.setSearchResult(resultHashMap);
        return searchResponseDTO;
    }

    //main method of this class
    public List<SearchResponseDTO> search(SearchSomethingDTO search) {
        List<SearchResponseDTO> searchResponseDTOS = new ArrayList<>();
        List<SiteType> siteTypes = extractSiteList(search);
        String productName = search.getProductName();
        String productType = search.getTypeName();

        if (siteTypes.contains(SiteType.MOBILE)) {
            SearchResponseDTO responseDTO = buildSearchResponse(mobileSearchResultScarper, SiteType.MOBILE, productName, productType);
            searchResponseDTOS.add(responseDTO);
        }

        if (siteTypes.contains(SiteType.DIGIKALA)) {
            SearchResponseDTO responseDTO = buildSearchResponse(digikalaSearchResultScarper, SiteType.DIGIKALA, productName, productType);
            searchResponseDTOS.add(responseDTO);
        }

        // ToDo: اضافه کردن سایت‌های دیگر در آینده
        return searchResponseDTOS;
    }
}
