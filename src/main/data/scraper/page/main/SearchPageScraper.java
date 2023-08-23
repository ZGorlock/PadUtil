/*
 * File:    SearchPageScraper.java
 * Package: main.data.scraper.page.main
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.scraper.page.main;

import java.util.List;
import java.util.Optional;

import commons.access.Internet;
import commons.lambda.stream.mapper.Mappers;
import main.data.mirror.host.ErrorResponse;
import main.data.scraper.page.PageScraper;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SearchPageScraper extends PageScraper {
    
    //Constants
    
    public static final String PAGE_CATEGORY_SEARCH = "search";
    
    
    //Methods
    
    @Override
    public Optional<List<String>> tryEnumerateUrls() {
        return Optional.of(List.of(getUrlRoot()));
    }
    
    
    //Getters
    
    @Override
    protected String getPageCategory() {
        return PAGE_CATEGORY_SEARCH;
    }
    
    
    //Static Methods
    
    public static int scrapePageCount() {
        return tryScrapePageCount()
                .orElseGet(ErrorResponse.invoke(-1, "Failed to scrape page count"));
    }
    
    public static Optional<Integer> tryScrapePageCount() {
        return Optional.of(URL_ROOT_PAGE)
                .map(pageId -> Mappers.perform(pageId, e -> System.out.println("Scraping page count: " + pageId)))
                .map(Internet::getHtml)
                .map(document -> document.getElementsByClass("mx-2 flex-grow-1 text-center"))
                .map(Elements::first).map(Element::text)
                .map(text -> text.replaceAll("(?i)results", "")).map(String::strip)
                .filter(text -> text.matches("^\\d+$"))
                .map(Integer::parseInt);
    }
    
}
