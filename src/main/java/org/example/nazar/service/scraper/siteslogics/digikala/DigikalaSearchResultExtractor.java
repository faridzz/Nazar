package org.example.nazar.service.scraper.siteslogics.digikala;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.DigikalaDTO;
import org.example.nazar.service.scraper.ISearchResultExtractor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DigikalaSearchResultExtractor implements ISearchResultExtractor<DigikalaDTO> {

    @Override
    public List<DigikalaDTO> extract(String responseBody) {
        List<DigikalaDTO> searchResults = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            // پارس کردن JSON پاسخ
            JsonNode rootNode = mapper.readTree(responseBody);
            JsonNode productsNode = rootNode.path("data").path("products");

            // پیمایش JSON و ساخت DTO ها
            for (JsonNode node : productsNode) {
                DigikalaDTO dto = new DigikalaDTO();
                try {
                    dto.setId(node.path("id").asInt());
                    dto.setTitle(node.path("title_en").asText());

                    // دریافت URL تصویر
                    JsonNode imageNode = node.path("images").path("main").path("url");
                    if (imageNode.isArray() && !imageNode.isEmpty()) {
                        dto.setImage(imageNode.get(0).asText());
                    } else {
                        log.warn("Image URL array is missing or empty for product ID: {}", dto.getId());
                    }

                    // دریافت URL محصول
                    JsonNode urlNode = node.path("url").path("uri");
                    if (urlNode.isTextual()) {
                        dto.setUrl(urlNode.asText());
                    } else {
                        log.warn("Product URL is missing or not a text for product ID: {}", dto.getId());
                    }

                } catch (Exception e) {
                    log.error("Error processing product ID: {} - {}", node.path("id").asInt(), e.getMessage());
                }
                searchResults.add(dto);
            }
        } catch (Exception e) {
            log.error("Error parsing JSON response: {}", e.getMessage());
        }

        return searchResults;
    }
}
