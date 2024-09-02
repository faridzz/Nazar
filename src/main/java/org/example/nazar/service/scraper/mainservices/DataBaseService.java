package org.example.nazar.service.scraper.mainservices;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.FullReviewDTO;
import org.example.nazar.exception.*;
import org.example.nazar.model.*;
import org.example.nazar.repository.*;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

/**
 * سرویس مدیریت محصولات و نظرات مرتبط با آن‌ها
 */
@Slf4j
@Service
public class DataBaseService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ProductReviewRepository productReviewRepository;
    private final SiteRepository siteRepository;
    private final TypeRepository typeRepository;

    public DataBaseService(ProductRepository productRepository,
                           ReviewRepository reviewRepository,
                           ProductReviewRepository productReviewRepository,
                           SiteRepository siteRepository,
                           TypeRepository typeRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.productReviewRepository = productReviewRepository;
        this.siteRepository = siteRepository;
        this.typeRepository = typeRepository;
    }

    /**
     * اضافه کردن یک محصول جدید به سیستم
     *
     * @param productName نام محصول جدید
     * @param type        تایپ محصول
     * @return محصول ذخیره شده در پایگاه داده
     */

    public Product addProduct(String productName, String type) {
        productName = productName.trim().toLowerCase(Locale.ROOT);
        type = type.trim().toLowerCase(Locale.ROOT);
        if (typeRepository.existsByName(type)) {
            Type productType = typeRepository.findByName(type);
            Product product = new Product();
            product.setName(productName);
            product.setType(productType);
            //اگر یه محصول تکراری بود پرتاب استثنا
            if (productRepository.existsByNameAndTypeName(productName, type)) {
                log.info("Product {} is already exists", productName);
                return productRepository.findByNameAndTypeName(productName, type);
            }
            return productRepository.save(product);
        }
        //اگر نوع محصول وجود نداشت پرتاب استثنا
        throw new ProductTypeNotFoundException();
    }

    /**
     * دریافت لیست نظرات مربوط به یک محصول خاص
     *
     * @param productName نام محصول
     * @return لیست نظرات محصول یا null اگر محصول یافت نشد
     */
    @Transactional(readOnly = true)
    public List<ProductReview> getReviewsForProduct(String productName) {
        productName = productName.trim().toLowerCase(Locale.ROOT);
        // یافتن محصول با شناسه مشخص شده
        String finalProductName = productName;
        Product product = null;
        try {
            product = productRepository.findByName(productName);

        } catch (Exception e) {
            throw new NotFoundException("Product not found", Product.class, finalProductName);
        }

        // نظرات مرتبط با محصول را برگردان
        return product.getProductReviews();
    }

    /**
     * @param url ادرس سایت رو وارد کن بدونه چیزا اضافه مثلا www.example.com
     * @return Site یه ابجکت از سایت رو برمیگردونه
     */
    @Transactional(readOnly = true)
    public Site getSiteByUrl(String url) {
        url = url.trim().toLowerCase(Locale.ROOT);
        Site site = siteRepository.findByUrl(url);
        if (site == null) {
            throw new NotFoundException("Site not found", Site.class, url);
        }
        return site;
    }

    /**
     * @param typeName نوع محصول ورودی باشه
     * @return Type یه ابجکت از تایپ رو برمیگردونه
     */
    @Transactional(readOnly = true)
    public Type getTypeByName(String typeName) {
        typeName = typeName.trim().toLowerCase(Locale.ROOT);
        Type type = typeRepository.findByName(typeName);
        if (type == null) {
            throw new NotFoundException("Type " + typeName + " not found");
        }
        return type;
    }

    /**
     * اضافه کردن یک نظر جدید به یک محصول و سایت مشخص
     *
     * @param fullReviewDTO نظر جدید با اطلاعات کامل درباره اون نظر
     * @return نظر محصول ذخیره شده در پایگاه داده یا null اگر محصول یا سایت یافت نشد
     */


    public ProductReview addReview(FullReviewDTO fullReviewDTO) {

        Review review = fullReviewDTO.getReview();
        String productName = fullReviewDTO.getProductName();
        String siteUrl = fullReviewDTO.getSiteUrl();
        String typeName = fullReviewDTO.getTypeName();
        Product product;
        // یافتن محصول و سایت با شناسه‌های مشخص شده
        try {
            product = productRepository.findByName(productName);
        } catch (IncorrectResultSizeDataAccessException e) {
            product = productRepository.findByNameAndTypeName(productName, typeName);
        } catch (Exception e) {
            throw new NotFoundException("error while find product with name " + productName);
        }

        Site site = siteRepository.findByUrl(siteUrl);
        if (site == null) {
            throw new NotFoundException("Site not found", Site.class, siteUrl);
        }
        // ساخت هش ای دی
        review.createHashId();
        log.debug(review.getHashId());
        //اگر هش ای دی تکراری بود
        if (existReviewByHashId(review.getHashId())) {
            throw new DuplicateHashIdException("Review with hashId " + review.getHashId() + " already exists.");
        }
        // تلاش برای ذخیره نظر جدید
        reviewRepository.save(review);


        // ایجاد یک نظر محصول جدید و مقداردهی آن
        ProductReview productReview = new ProductReview();
        productReview.setProduct(product);
        productReview.setSite(site);
        productReview.setReview(review);

        // ذخیره و برگرداندن نظر محصول جدید
        return productReviewRepository.save(productReview);
    }


    /**
     * اضافه کردن لیستی از نظرات جدید به یک محصول و سایت مشخص.
     * این متد نظرات را به صورت گروهی ذخیره می‌کند تا کارایی و عملکرد بهتری داشته باشد.
     *
     * @param reviews     لیست نظرات جدید
     * @param productName نام محصول
     * @param siteUrl     آدرس سایت
     * @return لیست نظرات محصول ذخیره شده در پایگاه داده
     * @throws NotFoundException اگر محصول یا سایت یافت نشد
     */
    @Transactional
    public List<ProductReview> addReviews(List<Review> reviews, String productName, String siteUrl) {
        // یافتن محصول با نام مشخص شده

        Product product = null;
        try {
            product = productRepository.findByName(productName);

        } catch (Exception e) {
            throw new NotFoundException("Product not found", Product.class, productName);
        }

        // یافتن سایت با آدرس مشخص شده
        Site site = siteRepository.findByUrl(siteUrl);
        if (site == null) {
            throw new NotFoundException("Site not found", Site.class, siteUrl);
        }
        reviewRepository.saveAll(reviews);  // ذخیره نظر جدید
        // تبدیل نظرات به ProductReview و ذخیره‌سازی گروهی
        // تبدیل نظرات به ProductReview و ذخیره‌سازی گروهی
        Product finalProduct = product;
        List<ProductReview> productReviews = reviews.parallelStream()
                .map(review -> {
                    ProductReview productReview = new ProductReview();
                    productReview.setProduct(finalProduct);
                    productReview.setSite(site);
                    productReview.setReview(review);
                    return productReview;
                })
                .toList();

        // ذخیره‌سازی گروهی نظرات محصول
        return productReviewRepository.saveAll(productReviews);
    }


    /**
     * اضافه کردن یک سایت جدید به سیستم
     *
     * @param site سایت جدید
     * @return سایت ذخیره شده در پایگاه داده
     */
    public Site addSite(Site site) {
        if (siteRepository.existsByUrl(site.getUrl())) {
            throw new DuplicateSiteException();
        }
        return siteRepository.save(site);
    }

    /**
     * اضافه کردن یک نوع جدید به سیستم
     *
     * @param type نوع جدید محصول
     * @return نوع محصول ذخیره شده در پایگاه داده
     */
    public Type addType(Type type) {
        // بررسی تکراری نبودن نام نوع محصول (در صورت نیاز)
        if (typeRepository.existsByName(type.getName())) {
            throw new DuplicateTypeException();
        }
        return typeRepository.save(type);
    }

    //
    public boolean existReviewByHashId(String hashId) {
        return reviewRepository.existsByHashId(hashId);
    }
}
