package org.example.nazar.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.nazar.enums.SiteType;

@Setter
@Getter
public class AddReviewAccurateDTO {
    private SiteType siteType;
    private String typeName;
    private BaseDTO baseDTO;

    @Override
    public String toString() {
        return "Search results {" +
                "siteType =" + siteType +
                ", type  =" + typeName +
                ", search =" + baseDTO +
                '}';
    }



}
