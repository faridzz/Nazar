package org.example.nazar.service.scraper.siteslogics.digikala;

import org.example.nazar.dto.DigikalaDTO;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class DigikalaSearchResultExtractorTest {


    @Test
    void testExtract_WithTrue_ResponseShouldReturnListOf_DigikalaDto() throws IOException {
        //Assign
        String filePath = "src/test/resources/testdata/digikalaJson.json";
        String jsonResponse = new String(Files.readAllBytes(Paths.get(filePath)));
        //Act
        DigikalaSearchResultExtractor extractor = new DigikalaSearchResultExtractor();
        List<DigikalaDTO> result = extractor.extract(jsonResponse);
        System.out.println(result);
        //Assertion
        assertThat(result.size()).
                as("result of digikala extractor should be more than zero").
                isPositive();
    }

    @Test
    void testExtract_WithFalse_ResponseShouldReturnEmptyList() {
        //Assign
        String data = "";
        //Act
        DigikalaSearchResultExtractor extractor = new DigikalaSearchResultExtractor();
        List<DigikalaDTO> result = extractor.extract(data);
        System.out.println(result);
        //Assertion
        assertThat(result.size()).
                as("result of digikala extractor should be zero").
                isZero();
    }
}