/*
 * File:    SearchPageParser.java
 * Package: main.data.parser.page.search
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.parser.page.search;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import main.data.parser.page.PageParser;
import main.data.scraper.page.main.SearchPageScraper;
import main.entity.base.form.SearchForm;
import main.entity.monster.awakening.Awakening;
import main.entity.monster.detail.Series;
import main.entity.monster.detail.Type;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchPageParser extends PageParser<SearchForm> {
    
    //Logger
    
    private static final Logger logger = LoggerFactory.getLogger(SearchPageParser.class);
    
    
    //Constants
    
    public static final String PAGE_CATEGORY_SEARCH = SearchPageScraper.PAGE_CATEGORY_SEARCH;
    
    
    //Methods
    
    @Override
    public Optional<SearchForm> tryParseData(File file) {
        return Optional.ofNullable(file)
                .map(this::parseSearchPage);
    }
    
    public SearchForm parseSearchPage(File page) {
        final SearchForm searchForm = new SearchForm();
        
        try {
            final Document doc = Jsoup.parse(page);
            
            searchForm.typeList = parseTypeSection(doc.selectFirst(":root *> select[name=type_c] + div"));
            searchForm.awakeningList = parseAwokenSkillSection(doc.selectFirst(":root *> div#collapse-aws"));
//            Element enemy = doc.selectFirst(":root *> div#collapseKw4");
//            Element team = doc.selectFirst(":root *> div#collapseKw2");
//            Element board = doc.selectFirst(":root *> div#collapseKw1");
//            Element orbsChange = doc.selectFirst(":root *> div#collapseKw6");
//            Element boardOrbsSpawn = doc.selectFirst(":root *> div#collapseKw5");
//            Element leaderSkill = doc.selectFirst(":root *> div#collapseKw3");
            searchForm.seriesList = parseSeriesSection(doc.selectFirst(":root *> div#series-search"));
//            Element advancedSorting = doc.selectFirst(":root *> div#advanced-search");
            
        } catch (Exception e) {
            logger.error("Failed to parse page: {}", page.getAbsolutePath(), e);
        }
        
        return searchForm;
    }
    
    private List<Type> parseTypeSection(Element section) {
        return Optional.ofNullable(section)
                .map(e -> e.select(":root > label"))
                .stream().flatMap(Collection::stream)
                .map(typeElement -> {
                    final String iconUrl = Optional.of(typeElement)
                            .map(e -> e.selectFirst(":root > img"))
                            .map(e -> e.attr("src"))
                            .orElse(null);
                    final Integer id = Optional.of(typeElement)
                            .map(e -> e.selectFirst("input"))
                            .map(e -> e.attr("value"))
                            .map(Integer::parseInt)
                            .orElse(null);
                    return new Type(id, iconUrl);
                })
                .collect(Collectors.toList());
    }
    
    private List<Awakening> parseAwokenSkillSection(Element section) {
        return Optional.ofNullable(section)
                .map(e -> e.select(":root > div > div > div.askill-container > img.awoken-icon"))
                .stream().flatMap(Collection::stream)
                .map(awokenSkillElement -> {
                    final String iconUrl = Optional.of(awokenSkillElement)
                            .map(e -> e.attr("src"))
                            .orElse(null);
                    final Integer id = Optional.of(awokenSkillElement)
                            .map(e -> e.attr("data-askill"))
                            .map(Integer::parseInt)
                            .orElse(null);
                    final String name = Optional.of(awokenSkillElement)
                            .map(e -> e.attr("title"))
                            .map(String::strip)
                            .orElse(null);
                    return new Awakening(id, name, iconUrl);
                })
                .collect(Collectors.toList());
    }
    
    private List<Series> parseSeriesSection(Element section) {
        return Optional.ofNullable(section)
                .map(e -> e.select(":root > div > div.custom-radio"))
                .stream().flatMap(Collection::stream)
                .map(seriesElement -> {
                    final Integer id = Optional.of(seriesElement)
                            .map(e -> e.selectFirst(":root > input"))
                            .map(e -> e.attr("value"))
                            .map(Integer::parseInt)
                            .orElse(null);
                    final String name = Optional.of(seriesElement)
                            .map(Element::text)
                            .orElse(null);
                    return new Series(id, name);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    protected Class<SearchForm> getTypeClass() {
        return SearchForm.class;
    }
    
    @Override
    protected String getCategory() {
        return PAGE_CATEGORY_SEARCH;
    }
    
    @Override
    protected Logger getLogger() {
        return logger;
    }
    
}
