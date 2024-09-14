package org.example.nazar.service.scraper.controllerservices;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.ReviewResultDTO;
import org.example.nazar.enums.SiteType;
import org.example.nazar.exception.NotFoundException;
import org.example.nazar.service.scraper.IReviewAdder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class SearchAndSaveFactory {

    private final SingleThreadOrMultiThread singleThreadOrMultiThread;
    private Map<IReviewAdder<BaseDTO>, String> reviewAdderMap = new HashMap<>();
    private final Set<IReviewAdder<BaseDTO>> relevantReviewAdders = new HashSet<>();

    public SearchAndSaveFactory(SingleThreadOrMultiThread singleThreadOrMultiThread,
                                AllSitesReviewAdder allSitesReviewAdder

    ) {
        this.singleThreadOrMultiThread = singleThreadOrMultiThread;
        this.reviewAdderMap = allSitesReviewAdder.reviewAdderMap;

    }

    public SearchAndSaveFactory getSiteName(List<String> siteList) {


        if (siteList.contains(SiteType.MOBILE.getUrl())) {
            reviewAdderMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(SiteType.MOBILE.name()))
                    .forEach(entry -> relevantReviewAdders.add(entry.getKey()));
        }

        if (siteList.contains(SiteType.DIGIKALA.getUrl())) {
            reviewAdderMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(SiteType.DIGIKALA.name()))
                    .forEach(entry -> relevantReviewAdders.add(entry.getKey()));
        }

        // بررسی اینکه اگر هیچ مقدار مرتبطی پیدا نشود، یک استثنا پرتاب شود
        if (relevantReviewAdders.isEmpty()) {
            throw new NotFoundException("Can't find any site because all entries are wrong.");
        }

        // اگر نیاز باشد، relevantReviewAdders را می‌توانید ذخیره کنید یا در مراحل بعدی از آن استفاده کنید.

        return this;
    }

    public List<ReviewResultDTO> searchAndAddToDatabase(BaseDTO baseDto, SiteType siteType, String typeName) throws IOException, InterruptedException, ExecutionException {
        List<ReviewResultDTO> reviewResultDTOS = new LinkedList<>();
        Set<IReviewAdder<BaseDTO>> iReviewAdders = singleThreadOrMultiThread
                .findMultiThreadOrSingleThread(baseDto, typeName, relevantReviewAdders);
        for (IReviewAdder<BaseDTO> reviewAdder : iReviewAdders) {
            reviewResultDTOS.add(reviewAdder.addReviewsToDatabase(baseDto, siteType, typeName));
        }
        return reviewResultDTOS;
    }
}
