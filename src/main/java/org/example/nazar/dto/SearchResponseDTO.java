package org.example.nazar.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.nazar.enums.SiteType;

import java.util.List;

@Getter
public class SearchResponseDTO {
    @Setter
    private SiteType siteType;
    private String typeName;
    @Setter
    private List<BaseDTO> baseDTO;

    @Override
    public String toString() {
        return "Search results {" +
                "siteType =" + siteType +
                ", type  =" + typeName +
                ", search =" + baseDTO +
                '}';
    }


    public void setTypeName(String typeName) {
        this.typeName = typeName.toLowerCase().trim();
    }
}
