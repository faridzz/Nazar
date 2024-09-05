package org.example.nazar.service.scraper.siteslogics.digikala;

import org.example.nazar.dto.DigikalaDTO;
import org.example.nazar.service.scraper.IReviewsScraper;
import org.example.nazar.service.scraper.SingleThreadReviewAdder;
import org.example.nazar.service.scraper.mainservices.DataBaseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DigikalaSingleThreadReviewAdder extends SingleThreadReviewAdder<DigikalaDTO> {
    public DigikalaSingleThreadReviewAdder(DataBaseService dataBaseService, @Qualifier("digikalaReviewsScraper") IReviewsScraper<DigikalaDTO> reviewsScraper) {
        super(dataBaseService, reviewsScraper);
    }
}
