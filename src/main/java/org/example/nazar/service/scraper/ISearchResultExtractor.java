package org.example.nazar.service.scraper;

import org.example.nazar.dto.BaseDTO;

import java.util.List;

public interface ISearchResultExtractor<T extends BaseDTO> {
    List<T> extract(String responseBody);
}
