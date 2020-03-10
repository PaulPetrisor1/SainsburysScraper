package com.sainsbury.scraper.service;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sainsbury.scraper.result.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SainsburysScrapeService {

    List<Product> buildProductsFromProductPage(HtmlPage htmlPage, String xPath);

    Optional<HtmlPage> getPageForUrl(String url);
}
