package org.example.nazar.service.scraper.controllerservices;

import org.example.nazar.dto.BaseDTO;
import org.example.nazar.enums.SiteType;
import org.example.nazar.service.scraper.IReviewAdder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AllSitesReviewAdder {
    final Map<IReviewAdder<BaseDTO>, String> reviewAdderMap = new HashMap<>();

    public AllSitesReviewAdder(
            @Qualifier("mobileMultiThreadReviewAdder") IReviewAdder mobileMultiThreadReviewAdder,
            @Qualifier("mobileSingleThreadReviewAdder") IReviewAdder mobileSingleThreadReviewAdder,
            @Qualifier("digikalaMultiThreadReviewAdder") IReviewAdder digikalaMultiThreadReviewAdder,
            @Qualifier("digikalaSingleThreadReviewAdder") IReviewAdder digikalaSingleThreadReviewAdder


    ) {

        reviewAdderMap.put(mobileMultiThreadReviewAdder, SiteType.MOBILE.name());
        reviewAdderMap.put(mobileSingleThreadReviewAdder, SiteType.MOBILE.name());
        reviewAdderMap.put(digikalaMultiThreadReviewAdder, SiteType.DIGIKALA.name());
        reviewAdderMap.put(digikalaSingleThreadReviewAdder, SiteType.DIGIKALA.name());

    }

}
