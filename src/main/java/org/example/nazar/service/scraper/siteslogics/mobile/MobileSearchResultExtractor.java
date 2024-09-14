package org.example.nazar.service.scraper.siteslogics.mobile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.BaseDTO;
import org.example.nazar.dto.MobileDTO;
import org.example.nazar.service.scraper.ISearchResultExtractor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MobileSearchResultExtractor implements ISearchResultExtractor {
    @Override
    public List<BaseDTO> extract(String responseBody) {

        List<BaseDTO> searchResults = new ArrayList<>();

        try {
            // پارس کردن JSON پاسخ
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(responseBody);

            // پیمایش JSON و ساخت DTO ها
            for (JsonNode node : rootNode) {
                MobileDTO dto = new MobileDTO();
                dto.setId(node.get("id").asLong());
                dto.setTitle(node.get("title").asText());
                dto.setImage(node.get("image").asText());
                dto.setUrl(node.get("url").asText());
                searchResults.add(dto);
            }
        } catch (JsonProcessingException e) {
            return searchResults;
        }
        return searchResults;
    }
}
