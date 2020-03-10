package com.sainsbury.scraper.service.parser;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sainsbury.scraper.result.Product;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductParserServiceServiceImpl implements ProductParserService {

    private final String XPATH_OF_PRODUCT_ANCHOR = "div[contains(@class, 'productInfo')]/div[contains(@class, 'productNameAndPromotions')]/h3/a";
    private final String XPATH_OF_PRODUCT_TITLE = "//div[contains(@class, 'productTitleDescriptionContainer')]/h1";
    private final String XPATH_FOR_NUTRITIONAL_VALUE = "//table[contains(@class, 'nutritionTable')]/tbody/tr[contains(@class, 'tableRow1')]/td[contains(@class, 'tableRow1')]";
    private final String XPATH_FOR_PRODUCT_PRICE = "//p[contains(@class, 'pricePerUnit')]";
    private final String XPATH_FOR_PRODUCT_DESCRIPTION = "//div[contains(@class, 'productText')]/p";

    @Override
    public Optional<Product> buildProduct(HtmlDivision htmlProduct) {

        if (htmlProduct == null) {
            return Optional.empty();
        }

        HtmlAnchor productAnchor = htmlProduct.getFirstByXPath(XPATH_OF_PRODUCT_ANCHOR);
        HtmlPage productPage = getProductPage(productAnchor);
        return Optional.of(
                new Product(
                        getUnformattedDataFromPageByXPath(productPage, XPATH_OF_PRODUCT_TITLE),
                        (int) getAsDouble(getDataFromPageByXPath(productPage, XPATH_FOR_NUTRITIONAL_VALUE)),
                        getAsDouble(getDataFromPageByXPath(productPage, XPATH_FOR_PRODUCT_PRICE)),
                        getUnformattedDataFromPageByXPath(productPage, XPATH_FOR_PRODUCT_DESCRIPTION)
                )
        );
    }

    private double getAsDouble(String value) {
        return value == null || value.isEmpty() ? 0.0 : Double.parseDouble(value);
    }

    private String getUnformattedDataFromPageByXPath(HtmlPage productPage, String xPath) {
        if (productPage == null) {
            return null;
        }

        HtmlElement htmlElement = productPage.getFirstByXPath(xPath);
        return htmlElement != null ? htmlElement.getTextContent().trim() : null;
    }

    private String getDataFromPageByXPath(HtmlPage productPage, String xPath) {
        if (productPage == null) {
            return null;
        }

        HtmlElement htmlElement = productPage.getFirstByXPath(xPath);
        return htmlElement != null ? getFormattedStringValue(htmlElement) : null;
    }

    private HtmlPage getProductPage(HtmlAnchor productAnchor) {
        if (productAnchor == null) {
            return null;
        }

        HtmlPage productPage;
        try {
            productPage = productAnchor.click();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return productPage;
    }

    private String getFormattedStringValue(HtmlElement htmlElement) {
        return htmlElement.getTextContent().trim().replaceAll("<0\\.5g",
                "0.5").replaceAll("kJ", "").replaceAll("£|/unit", "");
    }
}
