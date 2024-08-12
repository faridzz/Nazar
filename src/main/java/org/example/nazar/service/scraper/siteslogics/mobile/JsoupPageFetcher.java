package org.example.nazar.service.scraper.siteslogics.mobile;

import org.example.nazar.service.scraper.IPageFetcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
class JsoupPageFetcher implements IPageFetcher {
    @Override
    public Document fetchPage(URL url) throws IOException {
        return Jsoup.connect(url.toString()).get();
    }
}