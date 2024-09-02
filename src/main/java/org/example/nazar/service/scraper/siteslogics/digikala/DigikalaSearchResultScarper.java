package org.example.nazar.service.scraper.siteslogics.digikala;

import lombok.extern.slf4j.Slf4j;

import org.example.nazar.dto.DigikalaDTO;
import org.example.nazar.service.scraper.ISearchResultExtractor;
import org.example.nazar.service.scraper.SearchResultScarper;
import org.example.nazar.service.scraper.findbestmatch.FindBestMatchLevenshteinDistance;
import org.example.nazar.service.scraper.findbestmatch.IFindBestMatch;
import org.example.nazar.util.network.HttpGetRequestSender;
import org.example.nazar.util.network.IHttpRequestSender;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.util.List;

@Component
@Slf4j
public class DigikalaSearchResultScarper extends SearchResultScarper<DigikalaDTO> {
//    public static void main(String[] args) {
//        IHttpRequestSender iHttpRequestSender = new HttpGetRequestSender();
//        IFindBestMatch<DigikalaDTO> iFindBestMatch = new FindBestMatchLevenshteinDistance<>();
//        ISearchResultExtractor<DigikalaDTO> iSearchResultExtractor = new DigikalaSearchResultExtractor();
//        DigikalaSearchResultScarper digikalaSearchResultScarper = new
//                DigikalaSearchResultScarper(iHttpRequestSender, iFindBestMatch, iSearchResultExtractor);
//
//        log.info(digikalaSearchResultScarper.getSearchResults("xiaomi poco x6", "smartphone").toString());
//    }

    private final ISearchResultExtractor<DigikalaDTO> digikalaSearchResultExtractor;
    String SEARCH_URL_BASE = "https://api.digikala.com/v1/search/?q=";

    protected DigikalaSearchResultScarper(IHttpRequestSender httpGetRequestSender,
                                          IFindBestMatch<DigikalaDTO> findBestMatchLevenshteinDistance,
                                          @Qualifier("digikalaSearchResultExtractor") ISearchResultExtractor<DigikalaDTO> digikalaSearchResultExtractor) {
        super(httpGetRequestSender, findBestMatchLevenshteinDistance);
        this.digikalaSearchResultExtractor = digikalaSearchResultExtractor;
    }

    @Override
    protected String buildSearchUrl(String productName, String type) {
        if (type.trim().equalsIgnoreCase("smartphone")) {
            SEARCH_URL_BASE = "https://api.digikala.com/v1/categories/mobile-phone/search/?q=";
        }

        return SEARCH_URL_BASE + URLEncoder.encode(productName, StandardCharsets.UTF_8);
    }


    @Override
    protected List<DigikalaDTO> extractSearchResults(String responseBody) {
        return digikalaSearchResultExtractor.extract(responseBody);
    }

    @Override
    protected DigikalaDTO createDTOInstance() {
        return new DigikalaDTO();
    }

}
