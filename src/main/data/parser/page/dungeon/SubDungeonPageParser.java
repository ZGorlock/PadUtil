/*
 * File:    SubDungeonPageParser.java
 * Package: main.data.parser.page.dungeon
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.parser.page.dungeon;

import java.io.File;
import java.util.Optional;

import main.data.parser.page.PageParser;
import main.data.scraper.page.category.SubDungeonPageScraper;
import main.entity.base.resource.Image;
import main.entity.base.resource.Page;
import main.entity.base.resource.Resource;
import main.entity.dungeon.SubDungeon;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("SimplifyOptionalCallChains")
public class SubDungeonPageParser extends PageParser<SubDungeon> {
    
    //Logger
    
    private static final Logger logger = LoggerFactory.getLogger(SubDungeonPageParser.class);
    
    
    //Constants
    
    public static final String PAGE_CATEGORY_SUB_DUNGEON = SubDungeonPageScraper.PAGE_CATEGORY_SUB_DUNGEON;
    
    
    //Methods
    
    @Override
    public Optional<SubDungeon> tryParseData(File file) {
        return Optional.ofNullable(file)
                .map(this::parseSubDungeon);
    }
    
    public SubDungeon parseSubDungeon(File page) {
        final SubDungeon subDungeon = new SubDungeon();
        
        subDungeon.page = new Page(page);
        subDungeon.id = Optional.of(subDungeon.page)
                .map(Resource::getName).map(e -> e.replaceAll("\\D", ""))
                .map(Integer::parseInt).orElse(null);
        
        try {
            final Document doc = Jsoup.parse(page);
            final Element container = Optional.of(doc)
                    .map(e -> e.selectFirst("body > div.container"))
                    .orElseThrow();
            
            parseSubDungeonTitleSection(subDungeon, container);
            parseSubDungeonDetailsSection(subDungeon, container);
            
        } catch (Exception e) {
            logger.error("Failed to parse page: {}", page.getAbsolutePath(), e);
        }
        
        return subDungeon;
    }
    
    private void parseSubDungeonTitleSection(SubDungeon subDungeon, Element section) {
        Optional.ofNullable(section)
                .map(e -> e.selectFirst(":root > div.flex-wrap"))
                .ifPresent(titleSection -> {
                    
                    Optional.of(titleSection)
                            .map(e -> e.selectFirst(":root > img"))
                            .map(e -> e.attr("src"))
                            .ifPresent(iconUrl -> {
                                subDungeon.icon = new Image(iconUrl);
                            });
                    
                    Optional.of(titleSection)
                            .map(e -> e.selectFirst(":root > h1"))
                            .map(Element::text)
                            .ifPresent(name -> {
                                subDungeon.name = name;
                            });
                });
    }
    
    private void parseSubDungeonDetailsSection(SubDungeon subDungeon, Element section) {
        Optional.ofNullable(section)
                .map(e -> e.selectFirst(":root > p.text-muted"))
                .ifPresent(detailsSection -> {
                    
                    Optional.of(detailsSection)
                            .map(e -> e.selectFirst(":root > span:contains(Stamina)"))
                            .map(Element::text).map(e -> e.replaceAll("\\D", ""))
                            .map(Integer::parseInt)
                            .ifPresent(stamina -> {
                                subDungeon.stamina = stamina;
                            });
                    
                    Optional.of(detailsSection)
                            .map(e -> e.selectFirst(":root > span:contains(Floors)"))
                            .map(Element::text).map(e -> e.replaceAll("\\D", ""))
                            .map(Integer::parseInt)
                            .ifPresent(floors -> {
                                subDungeon.floorCount = floors;
                            });
                    
                    Optional.of(detailsSection)
                            .map(e -> e.selectFirst(":root > span:contains(Exp)"))
                            .map(Element::text).map(e -> e.replaceAll("\\D", ""))
                            .map(Long::parseLong)
                            .ifPresent(exp -> {
                                subDungeon.exp = exp;
                            });
                    
                    Optional.of(detailsSection)
                            .map(e -> e.selectFirst(":root > span:contains(Coin)"))
                            .map(Element::text).map(e -> e.replaceAll("\\D", ""))
                            .map(Long::parseLong)
                            .ifPresent(coins -> {
                                subDungeon.coins = coins;
                            });
                });
    }
    
    
    //Getters
    
    @Override
    protected String getTypeName(boolean plural) {
        return "Sub-Dungeon" + (plural ? "s" : "");
    }
    
    @Override
    protected Class<SubDungeon> getTypeClass() {
        return SubDungeon.class;
    }
    
    @Override
    protected String getCategory() {
        return PAGE_CATEGORY_SUB_DUNGEON;
    }
    
    @Override
    protected Logger getLogger() {
        return logger;
    }
    
}
