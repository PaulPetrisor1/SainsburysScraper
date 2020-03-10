package com.sainsbury.scraper.service.parser;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.sainsbury.scraper.result.Product;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ProductParserService {
    Optional<Product> buildProduct(HtmlDivision htmlProduct);

}
