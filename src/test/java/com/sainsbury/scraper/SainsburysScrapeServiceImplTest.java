package com.sainsbury.scraper;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sainsbury.scraper.result.Product;
import com.sainsbury.scraper.service.parser.ProductParserService;
import com.sainsbury.scraper.service.parser.ProductParserServiceServiceImpl;
import com.sainsbury.scraper.service.SainsburysScrapeService;
import com.sainsbury.scraper.service.SainsburysScrapeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SainsburysScrapeServiceImplTest {

    private final String SAINSBURYS_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";

    @Autowired
    private ProductParserService productParserService;

    @Autowired
    private SainsburysScrapeService sainsburysScrapeService;

    @BeforeEach
    void setUp() {
        sainsburysScrapeService = new SainsburysScrapeServiceImpl(productParserService);
    }


    @Test
    void givenValidUrl_whenParsingHtml_thenReturnOptionalOfNonEmptyHtmlPage() {
        Optional<HtmlPage> page = sainsburysScrapeService.getPageForUrl(SAINSBURYS_URL);

        assertTrue(page.isPresent());
    }

    @Test
    void givenInvalidUrl_whenParsingHtml_thenReturnOptionalOfNonEmptyHtmlPage() {
        Optional<HtmlPage> page = sainsburysScrapeService.getPageForUrl("zohaib.co.uk");

        assertFalse(page.isPresent());
    }


    @Test
    void givenNullHtmlPage_whenBuildingProductsFromProductPage_thenReturnEmptyList() {
        List<Product> productList = sainsburysScrapeService
                .buildProductsFromProductPage(null, "xpath");

        assertTrue(productList.isEmpty());
    }

    @Test
    void givenNullXPath_whenBuildingProductsFromProductPage_thenReturnEmptyList() {
        HtmlPage htmlPage = mock(HtmlPage.class);

        List<Product> productList = sainsburysScrapeService
                .buildProductsFromProductPage(htmlPage, null);

        assertTrue(productList.isEmpty());
    }

    @Test
    void givenEmptyXPath_whenBuildingProductsFromProductPage_thenReturnEmptyList() {
        HtmlPage htmlPage = mock(HtmlPage.class);

        List<Product> productList = sainsburysScrapeService
                .buildProductsFromProductPage(htmlPage, "");

        assertTrue(productList.isEmpty());
    }

    @Test
    void givenInvalidXPath_whenBuildingProductsFromProductPage_thenReturnEmptyList() {
        HtmlPage htmlPage = mock(HtmlPage.class);

        List<Product> productList = sainsburysScrapeService
                .buildProductsFromProductPage(htmlPage, "blah");

        assertTrue(productList.isEmpty());
    }

    @Test
    void givenValidXPath_andValidHtmlPage_whenBuildingProductsFromProductPage_thenReturnProductList() {
        ProductParserService productParserService = mock(ProductParserServiceServiceImpl.class);
        sainsburysScrapeService = new SainsburysScrapeServiceImpl(productParserService);
        HtmlPage htmlPage = mock(HtmlPage.class);
        HtmlDivision htmlDivision = mock(HtmlDivision.class);
        Product product = new Product("hehe", 1337, 13.37, "l33t product");
        doReturn(Collections.singletonList(htmlDivision)).when(htmlPage).getByXPath(anyString());
        when(productParserService.buildProduct(htmlDivision)).thenReturn(Optional.of(product));

//    when
        List<Product> productList = sainsburysScrapeService
                .buildProductsFromProductPage(htmlPage, "blah");

//    then
        assertFalse(productList.isEmpty());
    }

}