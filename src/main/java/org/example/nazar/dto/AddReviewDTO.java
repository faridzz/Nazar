package org.example.nazar.dto;

import lombok.*;

import java.util.Locale;


@NoArgsConstructor
@AllArgsConstructor
public class AddReviewDTO {


    String siteUrl;
    String typeName;
    String productName;

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl.toLowerCase(Locale.ROOT).trim();
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName.toLowerCase(Locale.ROOT).trim();
    }

    public void setProductName(String productName) {
        this.productName = productName.toLowerCase(Locale.ROOT).trim();
    }

    public String getSiteUrl() {
        return siteUrl.trim().toLowerCase(Locale.ROOT);
    }

    public String getTypeName() {
        return typeName.trim().toLowerCase(Locale.ROOT);
    }

    public String getProductName() {
        return productName.trim().toLowerCase(Locale.ROOT);
    }
}
