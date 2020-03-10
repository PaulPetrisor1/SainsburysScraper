package com.sainsbury.scraper;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sainsbury.scraper.service.parser.ProductParserServiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductParserServiceImplTest {

    @Autowired
    private ProductParserServiceServiceImpl productHtmlParser;

    private final String XPATH_OF_PRODUCT_ANCHOR = "div[contains(@class, 'productInfo')]/div[contains(@class, 'productNameAndPromotions')]/h3/a";
    private final String XPATH_OF_PRODUCT_TITLE = "//div[contains(@class, 'productTitleDescriptionContainer')]/h1";
    private final String XPATH_FOR_NUTRITIONAL_VALUE = "//table[contains(@class, 'nutritionTable')]/tbody/tr[contains(@class, 'tableRow1')]/td[contains(@class, 'tableRow1')]";
    private final String XPATH_FOR_PRODUCT_PRICE = "//p[contains(@class, 'pricePerUnit')]";


    @BeforeEach
    void setUp() {
        productHtmlParser = new ProductParserServiceServiceImpl();
    }

    @Test
    void givenNullHtmlProduct_whenParsingProduct_thenReturnEmpty() {
        assertEquals(Optional.empty(), productHtmlParser.buildProduct(null));
    }

    @Test
    void givenHtmlProductWithNoTitle_whenParsingProduct_thenReturnProductWithNullTitle() {
        HtmlDivision htmlProduct = mock(HtmlDivision.class);

        assertNull(productHtmlParser.buildProduct(htmlProduct).get().getTitle());
    }

    @Test
    void givenHtmlProductWithTitle_whenParsingProduct_thenReturnProductWithTitle() throws Exception {
        HtmlDivision htmlProduct = mock(HtmlDivision.class);
        HtmlAnchor htmlAnchor = mock(HtmlAnchor.class);
        HtmlPage page = mock(HtmlPage.class);
        HtmlElement titleElement = mock(HtmlElement.class);

        doReturn(htmlAnchor).when(htmlProduct).getFirstByXPath(XPATH_OF_PRODUCT_ANCHOR);
        when(htmlAnchor.click()).thenReturn(page);
        doReturn(titleElement).when(page).getFirstByXPath(XPATH_OF_PRODUCT_TITLE);
        when(titleElement.getTextContent()).thenReturn("htmlAnchorText");

        assertEquals("htmlAnchorText", productHtmlParser.buildProduct(htmlProduct).get().getTitle());
    }


    @Test
    void givenHtmlProductWithCalories_whenParsingProduct_thenReturnProductWithFormattedCalories() throws Exception {
        HtmlDivision htmlProduct = mock(HtmlDivision.class);
        HtmlAnchor htmlAnchor = mock(HtmlAnchor.class);
        HtmlPage page = mock(HtmlPage.class);
        HtmlElement htmlElement = mock(HtmlElement.class);

        doReturn(htmlAnchor).when(htmlProduct).getFirstByXPath(XPATH_OF_PRODUCT_ANCHOR);
        when(htmlAnchor.click()).thenReturn(page);
        doReturn(htmlElement).when(page).getFirstByXPath(XPATH_FOR_NUTRITIONAL_VALUE);
        when(htmlElement.getTextContent()).thenReturn("120kJ");

        assertEquals(120, productHtmlParser.buildProduct(htmlProduct).get().getkCalPer100g());
    }

    @Test
    void givenHtmlProductWithPrice_whenParsingProduct_thenReturnProductWithFormattedPrice() throws Exception {
        HtmlDivision htmlProduct = mock(HtmlDivision.class);
        HtmlAnchor htmlAnchor = mock(HtmlAnchor.class);
        HtmlPage page = mock(HtmlPage.class);
        HtmlElement htmlElement = mock(HtmlElement.class);

        doReturn(htmlAnchor).when(htmlProduct).getFirstByXPath(XPATH_OF_PRODUCT_ANCHOR);
        when(htmlAnchor.click()).thenReturn(page);
        doReturn(htmlElement).when(page).getFirstByXPath(XPATH_FOR_PRODUCT_PRICE);
        when(htmlElement.getTextContent()).thenReturn("£1.20");

        assertEquals(1.20, productHtmlParser.buildProduct(htmlProduct).get().getUnitPrice());
    }
}