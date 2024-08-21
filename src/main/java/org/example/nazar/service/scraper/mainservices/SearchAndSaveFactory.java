package org.example.nazar.service.scraper.mainservices;


import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.ReviewResultDTO;

import org.example.nazar.service.scraper.IReviewAdder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class SearchAndSaveFactory {

    private final Set<IReviewAdder> listOfSiteService = new HashSet<>();
    private final IReviewAdder mobile;

    public SearchAndSaveFactory(@Qualifier("mobileMultiThreadReviewAdder") IReviewAdder mobile) {
        this.mobile = mobile;
    }

    public SearchAndSaveFactory getSiteName(List<String> siteList) {
        siteList = siteList.stream().map(site -> site.toLowerCase(Locale.ROOT).trim()).toList();
        if (siteList.contains("www.mobile.ir")) {
            this.listOfSiteService.add(mobile);
        }
        // ToDo: Add other sites in the future ...
        if (listOfSiteService.isEmpty())
            throw new RuntimeException("cant find any thing because all entry site are wrong");
        return this;
    }

    public List<ReviewResultDTO> searchAndAddToDatabase(BaseDTO baseDto, String siteUrl, String typeName) {
        List<ReviewResultDTO> reviewResultDTOS = new LinkedList<>();
        log.debug(listOfSiteService.toString());
        for (IReviewAdder theClass : listOfSiteService) {
            reviewResultDTOS.add(theClass.addReviews(baseDto, siteUrl, typeName));
        }
        return reviewResultDTOS;
    }
}
