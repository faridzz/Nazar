package org.example.nazar.dto;

import lombok.*;
import org.example.nazar.enums.SiteType;

import java.util.Locale;


@NoArgsConstructor
@AllArgsConstructor
public class AddReviewDTO {

    @Getter
    @Setter
    SiteType siteType;
    String typeName;
    String productName;


    public void setTypeName(String typeName) {
        this.typeName = typeName.toLowerCase(Locale.ROOT).trim();
    }

    public void setProductName(String productName) {
        this.productName = productName.toLowerCase(Locale.ROOT).trim();
    }

    public String getTypeName() {
        return typeName.trim().toLowerCase(Locale.ROOT);
    }

    public String getProductName() {
        return productName.trim().toLowerCase(Locale.ROOT);
    }
}
