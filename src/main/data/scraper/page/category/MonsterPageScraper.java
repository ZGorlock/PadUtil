/*
 * File:    MonsterPageScraper.java
 * Package: main.data.scraper.page.category
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.scraper.page.category;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import main.data.scraper.page.PageScraper;
import main.data.scraper.page.main.SearchPageScraper;

public class MonsterPageScraper extends PageScraper {
    
    //Constants
    
    public static final String PAGE_CATEGORY_MONSTER = "monster";
    
    public static final Pattern URL_PATTERN_MONSTER = Pattern.compile("(?<url>(?<host>" + "/|" + Pattern.quote(URL_ROOT_PAGE) + ")" + "(?<location>(?<path>monster/|card/|)" + "(?<name>\\d+)" + "/?))");
    
    public static final Pattern URL_PATTERN_EXTRACTOR_MONSTER = Pattern.compile("(?<=\")" + URL_PATTERN_MONSTER.pattern() + "(?=\")");
    
    
    //Methods
    
    @Override
    public Optional<List<String>> tryEnumerateUrls() {
        return SearchPageScraper.tryScrapePageCount()
                .stream().flatMapToInt(e -> IntStream.rangeClosed(1, e)).boxed()
                .map(this::getPageUrl)
                .filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of));
    }
    
    @Override
    public Optional<String> getPageUrl(int id) {
        return Optional.of(id)
                .map(idNum -> (getUrlRoot() + idNum));
    }
    
    
    //Getters
    
    @Override
    protected String getPageCategory() {
        return PAGE_CATEGORY_MONSTER;
    }
    
    @Override
    protected Pattern getUrlPattern() {
        return URL_PATTERN_MONSTER;
    }
    
    @Override
    protected Pattern getUrlExtractorPattern() {
        return URL_PATTERN_EXTRACTOR_MONSTER;
    }
    
}
