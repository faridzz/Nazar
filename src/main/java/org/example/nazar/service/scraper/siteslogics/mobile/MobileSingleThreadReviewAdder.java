package org.example.nazar.service.scraper.siteslogics.mobile;

import org.example.nazar.dto.MobileDTO;
import org.example.nazar.service.scraper.SingleThreadReviewAdder;
import org.example.nazar.service.scraper.mainservices.DataBaseService;
import org.example.nazar.service.scraper.IReviewsScraper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MobileSingleThreadReviewAdder extends SingleThreadReviewAdder<MobileDTO> {
    public MobileSingleThreadReviewAdder(DataBaseService dataBaseService,
                                         @Qualifier("mobileReviewsScraper") IReviewsScraper<MobileDTO> mobileReviewsScraper) {
        super(dataBaseService, mobileReviewsScraper);
    }
}
