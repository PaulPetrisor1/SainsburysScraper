package com.sainsbury.scraper;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sainsbury.scraper.result.ListOfProducts;
import com.sainsbury.scraper.result.Product;
import com.sainsbury.scraper.service.SainsburysScrapeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@SpringBootApplication
public class ScraperApplication implements CommandLineRunner {

    private static final Logger LOGGER = Logger.getGlobal();
    private static final String SAINSBURYS_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
    private static final String XPATH_EXPR_FOR_PRODUCTS = "//ul[contains(@class, 'productLister gridView')]/li[contains(@class, gridItem)]/div[contains(@class, 'product')]";

    private SainsburysScrapeService sainsburysScrapeService;

    public ScraperApplication(SainsburysScrapeService sainsburysScrapeService) {
        this.sainsburysScrapeService = sainsburysScrapeService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ScraperApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Optional<HtmlPage> page = sainsburysScrapeService.getPageForUrl(SAINSBURYS_URL);
        if (!page.isPresent()) {
            LOGGER.severe("Sainsbury's page cannot be parsed. ");
            return;
        }

        List<Product> productList = sainsburysScrapeService
                .buildProductsFromProductPage(page.get(), XPATH_EXPR_FOR_PRODUCTS);

        Gson gson = new GsonBuilder().disableHtmlEscaping().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
        System.out.println(gson.toJson(new ListOfProducts(productList)));
    }
}
