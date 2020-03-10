package com.sainsbury.scraper.controler;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sainsbury.scraper.result.ListOfProducts;
import com.sainsbury.scraper.result.Product;
import com.sainsbury.scraper.service.SainsburysScrapeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    private static final String SAINSBURYS_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
    private static final String XPATH_EXPR_FOR_PRODUCTS = "//ul[contains(@class, 'productLister gridView')]/li[contains(@class, gridItem)]/div[contains(@class, 'product')]";
    private SainsburysScrapeService sainsburysScrapeService;

    public ProductController(SainsburysScrapeService sainsburysScrapeService) {
        this.sainsburysScrapeService = sainsburysScrapeService;
    }

    @GetMapping(value = "/getProducts", produces = MediaType.APPLICATION_JSON_VALUE)
    public String index() {
        Optional<HtmlPage> page = sainsburysScrapeService.getPageForUrl(SAINSBURYS_URL);
        List<Product> productList = sainsburysScrapeService
                .buildProductsFromProductPage(page.get(), XPATH_EXPR_FOR_PRODUCTS);

        Gson gson = new GsonBuilder().disableHtmlEscaping().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
        return gson.toJson(new ListOfProducts(productList));
    }
}
