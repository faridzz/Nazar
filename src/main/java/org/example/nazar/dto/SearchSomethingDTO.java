package org.example.nazar.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchSomethingDTO {
    private List<String> sitesUrl;
    private String productName;
    private String typeName;

}
