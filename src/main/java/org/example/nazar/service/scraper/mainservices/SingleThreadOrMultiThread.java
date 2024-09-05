package org.example.nazar.service.scraper.mainservices;

import org.example.nazar.dto.BaseDTO;
import org.example.nazar.repository.ProductRepository;
import org.example.nazar.service.scraper.IReviewAdder;
import org.example.nazar.service.scraper.MultiThreadReviewAdder;
import org.example.nazar.service.scraper.SingleThreadReviewAdder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SingleThreadOrMultiThread {

    private final ProductRepository productRepository;

    public SingleThreadOrMultiThread(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Set<IReviewAdder<BaseDTO>> findMultiThreadOrSingleThread(BaseDTO baseDto, String type, Set<IReviewAdder<BaseDTO>> iReviewAdders) {
        String nameOfProduct = baseDto.getTitle().trim().toLowerCase(Locale.ROOT);
        type = type.trim().toLowerCase(Locale.ROOT);

        boolean productExists = productRepository.existsByNameAndTypeName(nameOfProduct, type);

        return iReviewAdders.stream()
                .filter(iReviewAdder -> productExists
                        ? iReviewAdder instanceof MultiThreadReviewAdder
                        : iReviewAdder instanceof SingleThreadReviewAdder
                )
                .collect(Collectors.toSet());
    }
}
