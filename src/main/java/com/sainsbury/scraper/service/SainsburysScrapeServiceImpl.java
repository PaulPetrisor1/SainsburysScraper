package com.sainsbury.scraper.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sainsbury.scraper.result.Product;
import com.sainsbury.scraper.service.parser.ProductParserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class SainsburysScrapeServiceImpl implements SainsburysScrapeService {

    private static final Logger LOGGER = Logger.getGlobal();
    private ProductParserService productParserService;

    public SainsburysScrapeServiceImpl(ProductParserService productParserService) {
        this.productParserService = productParserService;
    }

    @Override
    public List<Product> buildProductsFromProductPage(HtmlPage htmlPage, String xPath) {
        if (htmlPage == null || xPath == null || xPath.isEmpty()) {
            LOGGER.severe("Cannot build products from product page: " + htmlPage + " xPath: " + xPath);
            return Collections.emptyList();
        }

        List<HtmlDivision> htmlProductList = (List<HtmlDivision>) htmlPage.getByXPath(xPath);
        ArrayList<Product> productList = new ArrayList<>();

        for (HtmlDivision htmlProduct : htmlProductList) {
            Optional<Product> productOpt = productParserService.buildProduct(htmlProduct);
            productOpt.ifPresent(productList::add);
        }
        return productList;
    }

    @Override
    public Optional<HtmlPage> getPageForUrl(String url) {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage page = null;
        try {
            page = webClient.getPage(url);
        } catch (Exception e) {
            e.getMessage();
        }
        return Optional.ofNullable(page);
    }
}
