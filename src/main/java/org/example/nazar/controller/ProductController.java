package org.example.nazar.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.*;
import org.example.nazar.model.Product;
import org.example.nazar.model.ProductReview;
import org.example.nazar.model.Site;
import org.example.nazar.model.Type;
import org.example.nazar.service.scraper.mainservices.DataBaseService;
import org.example.nazar.service.scraper.controllerservices.FindAndAddReviewToDatabase;
import org.example.nazar.service.scraper.mainservices.SearchResults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Slf4j
@RestController
@RequestMapping("/api")
//خطاها توسط کلاسGlobalControllerExceptionHandler هندل میشوند
public class ProductController {

    private final DataBaseService dataBaseService;


    private final SearchResults searchResults;
    private final FindAndAddReviewToDatabase findAndAddReviewToDatabase;

    public ProductController(DataBaseService dataBaseService
            , SearchResults searchResults, FindAndAddReviewToDatabase findAndAddReviewToDatabase) {
        this.dataBaseService = dataBaseService;
        this.searchResults = searchResults;
        this.findAndAddReviewToDatabase = findAndAddReviewToDatabase;
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

    @PostMapping("/search")
    public ResponseEntity<List<SearchResponseDTO>> search(@RequestBody SearchSomethingDTO search) {
        List<SearchResponseDTO> searchResponseDTOs = searchResults.search(search);
        if (searchResponseDTOs != null) {
            return ResponseEntity.ok(searchResponseDTOs);
        } else {
            return ResponseEntity.notFound().build();
        }


    }

    @PostMapping("/add/one/accurate")
    public ResponseEntity<String> addReviewAccurate(@RequestBody List<AddReviewAccurateDTO> addReviewAccurateDTOs) {
        List<ReviewResultDTO> reviewResultDTOS = new ArrayList<>();
        for (AddReviewAccurateDTO addReviewAccurateDTO : addReviewAccurateDTOs) {
            reviewResultDTOS.add(findAndAddReviewToDatabase.findAccurately(addReviewAccurateDTO));
        }
        return ResponseEntity.ok(reviewResultDTOS.toString());

    }


    @PostMapping("/add/one/fast")
    public ResponseEntity<String> addReviewDirectly(@RequestBody AddReviewDTO addReviewDTO) {

        return findAndAddReviewToDatabase.addReviewDirectly(addReviewDTO);
    }
}




