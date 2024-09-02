package org.example.nazar.service.scraper.siteslogics.mobile;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.MobileDTO;
import org.example.nazar.exception.NotFoundException;
import org.example.nazar.service.scraper.ISearchResultExtractor;

import org.example.nazar.service.scraper.SearchResultScarper;
import org.example.nazar.service.scraper.findbestmatch.IFindBestMatch;
import org.example.nazar.util.network.IHttpRequestSender;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.util.List;


@Slf4j
@Component

// کلاس اسکرپینگ نتایج جستجو برای موبایل
public class MobileSearchResultScarper extends SearchResultScarper<MobileDTO> {
    private static final String SEARCH_URL_BASE = "https://www.mobile.ir/phones/ajaxphonesearch.aspx?q=";
    private final ISearchResultExtractor<MobileDTO> mobileSearchResultsExtractor;

    protected MobileSearchResultScarper(IHttpRequestSender httpGetRequestSender,
                                        IFindBestMatch<MobileDTO> findBestMatchLevenshteinDistance,
                                        @Qualifier("mobileSearchResultExtractor") ISearchResultExtractor<MobileDTO> mobileSearchResultsExtractor) {
        super(httpGetRequestSender, findBestMatchLevenshteinDistance);
        this.mobileSearchResultsExtractor = mobileSearchResultsExtractor;
    }


    @Override
    protected String buildSearchUrl(String productName, String type) {
        if (type.trim().equalsIgnoreCase("smartphone")) {
            return SEARCH_URL_BASE + URLEncoder.encode(productName, StandardCharsets.UTF_8);
        } else {
            throw new NotFoundException("this type : " + type + " not found");
        }
    }

    @Override
    protected List<MobileDTO> extractSearchResults(String responseBody) {
        return mobileSearchResultsExtractor.extract(responseBody);
    }


    @Override
    protected MobileDTO createDTOInstance() {
        return new MobileDTO();
    }


}
