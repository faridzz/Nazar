package org.example.nazar.service.scraper.siteslogics.mobile;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.MobileDTO;
import org.example.nazar.service.scraper.IReviewsScraper;
import org.example.nazar.service.scraper.mainservices.DataBaseService;
import org.example.nazar.service.scraper.MultiThreadReviewAdder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MobileMultiThreadReviewAdder extends MultiThreadReviewAdder<MobileDTO> {


    public MobileMultiThreadReviewAdder(DataBaseService dataBaseService, @Qualifier("mobileReviewsScraper") IReviewsScraper<MobileDTO> reviewsScraper) {
        super(dataBaseService, reviewsScraper, 60);
    }
}
