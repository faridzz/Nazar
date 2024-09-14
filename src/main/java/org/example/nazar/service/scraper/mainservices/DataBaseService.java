package org.example.nazar.service.scraper.mainservices;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.FullReviewDTO;
import org.example.nazar.enums.SiteType;
import org.example.nazar.exception.*;
import org.example.nazar.model.*;
import org.example.nazar.repository.*;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Service class for managing products and their associated reviews.
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
     * Adds a new product to the system.
     *
     * @param productName the name of the new product.
     * @param type        the type of the product.
     * @return the saved product in the database.
     */
    public Product addProduct(String productName, String type) {
        // Normalize product name and type by trimming and converting to lowercase
        productName = productName.trim().toLowerCase(Locale.ROOT);
        type = type.trim().toLowerCase(Locale.ROOT);

        // Check if the type exists
        if (typeRepository.existsByName(type)) {
            Type productType = typeRepository.findByName(type);

            // Create a new Product entity
            Product product = new Product();
            product.setName(productName);
            product.setType(productType);

            // If the product already exists, return the existing product
            if (productRepository.existsByNameAndTypeName(productName, type)) {
                log.info("Product {} already exists", productName);
                return productRepository.findByNameAndTypeName(productName, type);
            }

            // Save and return the new product
            return productRepository.save(product);
        }

        // Throw an exception if the product type is not found
        throw new ProductTypeNotFoundException();
    }

    /**
     * Retrieves the list of reviews for a specific product.
     *
     * @param productName the name of the product.
     * @return the list of product reviews, or null if the product is not found.
     */
    @Transactional(readOnly = true)
    public List<ProductReview> getReviewsForProduct(String productName) {
        // Normalize product name by trimming and converting to lowercase
        productName = productName.trim().toLowerCase(Locale.ROOT);

        // Find the product by name
        Product product;
        try {
            product = productRepository.findByName(productName);
        } catch (Exception e) {
            throw new NotFoundException("Product not found", Product.class, productName);
        }

        // Return the associated product reviews
        return product.getProductReviews();
    }

    /**
     * Adds a new review to a specific product and site.
     *
     * @param fullReviewDTO the review DTO containing the review, product name, and site URL.
     * @return the saved product review in the database.
     */
    public ProductReview addReview(FullReviewDTO fullReviewDTO) {
        // Extract review, product name, site URL, and type name from DTO
        Review review = fullReviewDTO.getReview();
        String productName = fullReviewDTO.getProductName();
        String siteUrl = fullReviewDTO.getSiteUrl();
        String typeName = fullReviewDTO.getTypeName();
        Product product;

        // Attempt to find the product by name and type, handling exceptions
        try {
            product = productRepository.findByName(productName);
        } catch (IncorrectResultSizeDataAccessException e) {
            product = productRepository.findByNameAndTypeName(productName, typeName);
        } catch (Exception e) {
            throw new NotFoundException("Error while finding product with name " + productName);
        }

        // Find the site by URL, throwing an exception if not found
        Site site = siteRepository.findByUrl(siteUrl);
        if (site == null) {
            throw new NotFoundException("Site not found", Site.class, siteUrl);
        }

        // Generate a hash ID for the review
        review.createHashId();
        log.debug(review.getHashId());

        // Check if a review with the same hash ID already exists
        if (existReviewByHashId(review.getHashId())) {
            throw new DuplicateHashIdException("Review with hashId " + review.getHashId() + " already exists.");
        }

        // Save the new review to the database
        reviewRepository.save(review);

        // Create and populate a new ProductReview entity
        ProductReview productReview = new ProductReview();
        productReview.setProduct(product);
        productReview.setSite(site);
        productReview.setReview(review);

        // Save and return the new ProductReview
        return productReviewRepository.save(productReview);
    }

    /**
     * Adds a list of new reviews to a specific product and site in bulk.
     *
     * @param reviews     the list of new reviews.
     * @param productName the name of the product.
     * @param siteUrl     the URL of the site.
     * @return the list of saved product reviews.
     * @throws NotFoundException if the product or site is not found.
     */
    @Transactional
    public List<ProductReview> addReviews(List<Review> reviews, String productName, String siteUrl) {
        // Find the product by name
        Product product;
        try {
            product = productRepository.findByName(productName);
        } catch (Exception e) {
            throw new NotFoundException("Product not found", Product.class, productName);
        }
        //Check if a review with the null created at and fix it
        reviews.parallelStream().filter(review -> review.getCreatedAt() == null).forEach(review ->
                review.setCreatedAt(LocalDate.now())
        );
        // Generate hash IDs for each review in parallel
        reviews.parallelStream().forEach(Review::createHashId);

        // Find the site by URL, throwing an exception if not found
        Site site = siteRepository.findByUrl(siteUrl);
        if (site == null) {
            throw new NotFoundException("Site not found", Site.class, siteUrl);
        }


        // Save the reviews in bulk to the database
        reviewRepository.saveAll(reviews);

        // Convert reviews to ProductReview entities and associate them with the product and site
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

        // Save and return the list of ProductReviews
        return productReviewRepository.saveAll(productReviews);
    }

    /**
     * Adds a new site to the system.
     *
     * @param site the new site to add.
     * @return the saved site in the database.
     */
    public Site addSite(Site site) {
        // Check if the site already exists by URL
        if (siteRepository.existsByUrl(site.getUrl())) {
            throw new DuplicateSiteException();
        }
        // Save and return the new site
        return siteRepository.save(site);
    }

    /**
     * Adds a new site to the system by SiteType.
     *
     * @param site the new site represented by SiteType.
     */
    public void addSite(SiteType site) {
        // Check if the site already exists by URL
        if (siteRepository.existsByUrl(site.getUrl())) {
            siteRepository.findByUrl(site.getUrl());
            return;
        }

        // Create a new Site entity and set its fields
        Site siteBuilder = new Site();
        siteBuilder.setName(site.name());
        siteBuilder.setUrl(site.getUrl());

        // Save and return the new site
        siteRepository.save(siteBuilder);
    }

    /**
     * Adds a new product type to the system.
     *
     * @param type the new product type to add.
     * @return the saved product type in the database.
     */
    public Type addType(Type type) {
        // Check if the product type already exists
        if (typeRepository.existsByName(type.getName())) {
            throw new DuplicateTypeException();
        }

        // Save and return the new product type
        return typeRepository.save(type);
    }

    /**
     * Checks if a review with a given hash ID already exists.
     *
     * @param hashId the hash ID of the review.
     * @return true if the review exists, false otherwise.
     */
    public boolean existReviewByHashId(String hashId) {
        return reviewRepository.existsByHashId(hashId);
    }
}
