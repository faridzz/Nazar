package org.example.nazar.dto;

import lombok.Data;
import org.example.nazar.enums.SiteType;

import java.util.HashMap;
import java.util.List;

@Data
public class SearchResponseDTO {
    private SiteType siteType;
    private HashMap<String, String> searchResult;//-> <title , product url>

    @Override
    public String toString() {
        return "Search results {" +
                "siteType =" + siteType +
                ", search =" + searchResult +
                '}';
    }
}
