package org.example.nazar.service.scraper;

import org.example.nazar.dto.BaseDTO;
import org.example.nazar.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public interface ISearchResultScarper {
    List<BaseDTO> getSearchResults(String productName);

    Optional<BaseDTO> findBestResult(List<BaseDTO> results);
}
