package org.example.nazar.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.AddReviewDTO;
import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.FullReviewDTO;

import org.example.nazar.dto.ReviewResultDTO;
import org.example.nazar.exception.DuplicateHashIdException;
import org.example.nazar.model.*;
import org.example.nazar.service.scraper.IReviewAdder;
import org.example.nazar.service.scraper.mainservices.DataBaseService;
import org.example.nazar.service.scraper.ISearchAndAddReviewToDataBase;
import org.example.nazar.service.scraper.ISearchResultScarper;

import org.example.nazar.service.scraper.mainservices.SearchAndSaveFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

import static org.example.nazar.util.stringbuilder.BuildStringForController.buildErrorResponse;
import static org.example.nazar.util.stringbuilder.BuildStringForController.buildResultString;

@Slf4j
@RestController
@RequestMapping("/api")
//خطاها توسط کلاسGlobalControllerExceptionHandler هندل میشوند
public class ProductController {

    private final DataBaseService dataBaseService;

    private final ISearchResultScarper iSearchResultScarper;
    private final IReviewAdder iReviewAdder;
    private final SearchAndSaveFactory searchAndSaveFactory;

    public ProductController(DataBaseService dataBaseService,
                             @Qualifier("mobileSearchResultScarper") ISearchResultScarper iSearchResultScarper,
                             @Qualifier("mobileMultiThreadReviewAdder") IReviewAdder iReviewAdder, SearchAndSaveFactory searchAndSaveFactory) {
        this.dataBaseService = dataBaseService;
        this.iSearchResultScarper = iSearchResultScarper;
        this.iReviewAdder = iReviewAdder;
        this.searchAndSaveFactory = searchAndSaveFactory;
    }

    @GetMapping
    public String test() {
        return "test";
    }

    /**
     * محصول جدیدی را به سیستم اضافه می‌کند.
     *
     * @param productName نام محصولی که می‌خواهید اضافه شود
     * @param type        نام نوع محصول (مثلاً "کتاب"، "لپ‌تاپ") توجه داشته باشید که این نوع باید از قبل در سیستم ثبت شده باشد
     * @return اطلاعات محصول ذخیره شده در صورت موفقیت، یا پیام خطا در صورت عدم وجود نوع محصول
     */
    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(
            @RequestParam String productName,
            @RequestParam String type) throws Exception {

        Product savedProduct = dataBaseService.addProduct(productName, type);
        return ResponseEntity.ok(savedProduct);

    }

    /**
     * نظرات مربوط به یک محصول خاص را دریافت می‌کند.
     *
     * @param productName نام محصول مورد نظر
     * @return لیست نظرات مربوط به محصول در صورت وجود، یا پیام خطای "پیدا نشد"
     */
    @GetMapping("/products/{productName}/reviews")
    public ResponseEntity<List<ProductReview>> getReviewsForProduct(@PathVariable String productName) {
        List<ProductReview> reviews = dataBaseService.getReviewsForProduct(productName);
        return reviews != null ? ResponseEntity.ok(reviews) : ResponseEntity.notFound().build();
    }

    /**
     * نظر جدیدی را به یک محصول و سایت مشخص اضافه می‌کند.
     * ٌ@param fullReviewDTO که شامل موارد زیر است
     * review اطلاعات نظر (متن، امتیاز و ...)
     * productName شناسه محصول مورد نظر
     * siteUrl شناسه سایت مربوط به نظر
     *
     * @return اطلاعات نظر ذخیره شده در صورت موفقیت، یا پیام خطا در صورت بروز مشکل
     */
    @PostMapping("/reviews")
    public ResponseEntity<String> addReview(@RequestBody FullReviewDTO fullReviewDTO) {
        ProductReview savedReview = dataBaseService.addReview(fullReviewDTO);
        return ResponseEntity.ok().body(savedReview.toString());


    }

    /**
     * سایت جدیدی را به سیستم اضافه می‌کند.
     *
     * @param site اطلاعات سایت (نام، آدرس و ...)
     * @return اطلاعات سایت ذخیره شده
     */
    @PostMapping("/sites")
    public ResponseEntity<Site> addSite(@RequestBody Site site) {

        Site savedSite = dataBaseService.addSite(site);
        return ResponseEntity.ok(savedSite);

    }

    /**
     * نوع جدیدی از محصول را به سیستم اضافه می‌کند.
     *
     * @param type اطلاعات نوع محصول (نام و ...)
     * @return اطلاعات نوع محصول ذخیره شده در صورت موفقیت، یا پیام خطا در صورت تکراری بودن نوع
     */
    @PostMapping("/types") // تغییر مسیر به "/types" برای تطابق با استاندارد restfull
    public ResponseEntity<Type> addType(@RequestBody Type type) {
        Type savedType = dataBaseService.addType(type);
        return ResponseEntity.ok(savedType);
    }


    /**
     * اضافه کردن نظرات یک محصول تنها با نام دقیق اون محصول د
     *
     * @param addReviewDTO شامل اطلاعات نظر، نام محصول و آدرس سایت
     * @return تعداد نظرات اضافه شده در صورت موفقیت، یا پیام خطا در صورت بروز مشکل
     */
    @PostMapping("/add/one")
    public ResponseEntity<String> addReview(@RequestBody AddReviewDTO addReviewDTO) {
        String productNameSearchInput = addReviewDTO.getProductName();
        String productNameSearchResult = "";

        try {
            // دریافت لیست نتایج جستجو برای محصول مورد نظر
            List<BaseDTO> listOfProductResults = iSearchResultScarper.getSearchResults(productNameSearchInput);
            Optional<BaseDTO> bestResult = iSearchResultScarper.findBestResult(listOfProductResults);

            if (bestResult.isPresent()) {
                productNameSearchResult = bestResult.get().getTitle();
                // دریافت و اضافه کردن نظرات
                List<ReviewResultDTO> reviewResultDTOS = searchAndSaveFactory
                        .getSiteName(List.of("www.mobile.ir"))
                        .searchAndAddToDatabase(bestResult.get(), addReviewDTO.getSiteUrl(), addReviewDTO.getTypeName());

                // ساختن نتیجه خروجی
                String resultString = buildResultString(reviewResultDTOS, productNameSearchInput, productNameSearchResult);

                return ResponseEntity.ok(resultString);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error adding review from specific site", e);
            return buildErrorResponse(productNameSearchInput, productNameSearchResult, e);
        }
    }


}

