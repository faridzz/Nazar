package org.example.nazar.service.scraper;

import org.example.nazar.dto.BaseDTO;

import java.util.List;

public interface ISearchResultExtractor {
    List<BaseDTO> extract(String responseBody);
}
