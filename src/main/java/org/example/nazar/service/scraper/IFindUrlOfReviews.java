package org.example.nazar.service.scraper;

import org.example.nazar.dto.BaseDTO;


import java.net.URL;
import java.util.Optional;

public interface IFindUrlOfReviews {
    Optional<URL> getUrlOfProduct(BaseDTO product);
}
