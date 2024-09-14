package org.example.nazar.service.scraper.controllerservices;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.*;
import org.example.nazar.enums.SiteType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.example.nazar.util.stringbuilder.BuildStringForController.buildErrorResponse;
import static org.example.nazar.util.stringbuilder.BuildStringForController.buildResultString;

@Component
@Slf4j
public class FindAndAddReviewToDatabase {
    private final SearchAndSaveFactory searchAndSaveFactory;

    public FindAndAddReviewToDatabase(SearchAndSaveFactory searchAndSaveFactory) {
        this.searchAndSaveFactory = searchAndSaveFactory;
    }

    public ReviewResultDTO findAccurately(AddReviewAccurateDTO addReviewAccurateDTO) {
        String typeName = addReviewAccurateDTO.getTypeName();
        SiteType siteType = addReviewAccurateDTO.getSiteType();
        String siteUrl = siteType.getUrl();
        BaseDTO baseDTO;
        if (SiteType.DIGIKALA.getUrl().equals(siteUrl)) {
            //crate a digikalaDTO from addReviewAccurateDTO(baseDTO)
            baseDTO = new DigikalaDTO(addReviewAccurateDTO.getBaseDTO());
        } else if (SiteType.MOBILE.getUrl().equals(siteUrl)) {
            //create a mobileDTO from addReviewAccurateDTO(baseDTO)
            baseDTO = new MobileDTO(addReviewAccurateDTO.getBaseDTO());
        } else {
            return new ReviewResultDTO(siteUrl, 0L, 0);
        }

        List<ReviewResultDTO> reviewResultDTOS;
        try {
            //search and add and return number of reviews who added
            reviewResultDTOS = searchAndSaveFactory
                    .getSiteName(List.of(siteUrl))
                    .searchAndAddToDatabase(baseDTO, siteType, typeName);


        } catch (Exception e) {
            return new ReviewResultDTO(siteUrl, 0L, 0);
        }
        return reviewResultDTOS.get(0);
    }

    public ResponseEntity<String> addReviewDirectly(AddReviewDTO addReviewDTO) {
        String productNameSearchInput = addReviewDTO.getProductName();
        String productNameSearchResult = "";
        BaseDTO searchedDTO = new BaseDTO();
        productNameSearchResult = searchedDTO.getTitle();
        try {
            // دریافت و اضافه کردن نظرات
            List<ReviewResultDTO> reviewResultDTOS = searchAndSaveFactory
                    .getSiteName(List.of(SiteType.MOBILE.name()))
                    .searchAndAddToDatabase(searchedDTO, addReviewDTO.getSiteType(), addReviewDTO.getTypeName());
            // ساختن نتیجه خروجی
            String resultString = buildResultString(reviewResultDTOS, productNameSearchInput, productNameSearchResult);

            return ResponseEntity.ok(resultString);
        } catch (Exception e) {
            log.error("Error adding review from specific site", e);
            return buildErrorResponse(productNameSearchInput, productNameSearchResult, e);
        }
    }
}

