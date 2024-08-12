package org.example.nazar.service.scraper.siteslogics.mobile;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.MobileDTO;
import org.example.nazar.service.scraper.ISearchResultExtractor;
import org.example.nazar.service.scraper.ISearchResultScarper;
import org.example.nazar.service.scraper.findbestmatch.IFindBestMatch;
import org.example.nazar.util.network.IHttpRequestSender;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
// کلاس اسکرپینگ نتایج جستجو برای موبایل
public class MobileSearchResultScarper implements ISearchResultScarper {
    private static final String SEARCH_URL_BASE = "https://www.mobile.ir/phones/ajaxphonesearch.aspx?q=";
    private final IHttpRequestSender httpGetRequestSender;
    private final IFindBestMatch findBestMatchLevenshteinDistance;
    private final ISearchResultExtractor searchResultExtractor;

    public MobileSearchResultScarper(IHttpRequestSender httpGetRequestSender,
                                     IFindBestMatch findBestMatchLevenshteinDistance, ISearchResultExtractor searchResultExtractor) {
        this.httpGetRequestSender = httpGetRequestSender;
        this.findBestMatchLevenshteinDistance = findBestMatchLevenshteinDistance;
        this.searchResultExtractor = searchResultExtractor;
    }


    // متدی برای پردازش نتایج جستجو و تبدیل آن به لیستی از DTO ها
    @Override
    public List<BaseDTO> getSearchResults(String productName) {
        // ساخت URI جستجو با انکدینگ مناسب
        String searchUrl = SEARCH_URL_BASE + URLEncoder.encode(productName, StandardCharsets.UTF_8);
        List<BaseDTO> searchResults;
        String responseBody;
        try {
            // ارسال درخواست و دریافت پاسخ
            responseBody = httpGetRequestSender.sendRequest(searchUrl);
        } catch (IOException | InterruptedException e) {
            log.error("Error while sending request", e);
            // بازگرداندن یک لیست خالی در صورت بروز خطا
            return new ArrayList<>();
        }

        searchResults = searchResultExtractor.extract(responseBody);

        if (searchResults.isEmpty()) {
            log.error("No search results found");
            return new ArrayList<>();
        }
        //اضافه کردن متن سرچ شده به اخر لیست
        MobileDTO mobileDTO = new MobileDTO();
        mobileDTO.setTitle(productName);
        searchResults.add(mobileDTO);

        // بازگرداندن لیست نتایج جستجو
        return searchResults;

    }

    @Override
    public Optional<BaseDTO> findBestResult(List<BaseDTO> results) {
        if (results.isEmpty()) {
            log.error("No results found");
            return Optional.empty();
        } else {
            String searchedString = results.get(results.size() - 1).getTitle();
            return findBestMatchLevenshteinDistance.finder(results, searchedString);
        }
    }
}
