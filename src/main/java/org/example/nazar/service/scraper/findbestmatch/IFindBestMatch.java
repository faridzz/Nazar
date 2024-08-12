package org.example.nazar.service.scraper.findbestmatch;

import org.example.nazar.dto.BaseDTO;

import java.util.List;
import java.util.Optional;

public interface IFindBestMatch {
    Optional<BaseDTO> finder(List<BaseDTO> dto , String search);
}
