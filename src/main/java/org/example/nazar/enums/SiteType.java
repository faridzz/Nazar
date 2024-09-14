package org.example.nazar.enums;

import java.util.Arrays;

public enum SiteType {
    MOBILE("www.mobile.ir"),
    DIGIKALA("www.digikala.com");

    private final String url;

    SiteType(String url) {
        this.url = url;
    }

    public static SiteType getSiteType(String siteUrl) {
        return Arrays.stream(values())
                .filter(siteType -> siteUrl.toLowerCase().toLowerCase().contains(siteType.url))
                .findFirst()
                .orElse(null);
    }

    public String getUrl() {
        return this.url;
    }
}