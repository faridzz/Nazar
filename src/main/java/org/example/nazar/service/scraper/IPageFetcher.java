package org.example.nazar.service.scraper;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

public interface IPageFetcher {
    Document fetchPage(URL url) throws IOException;
}