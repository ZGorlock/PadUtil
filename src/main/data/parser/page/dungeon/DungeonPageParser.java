/*
 * File:    DungeonPageParser.java
 * Package: main.data.parser.page.dungeon
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.parser.page.dungeon;

import java.io.File;
import java.util.Optional;

import main.data.parser.page.PageParser;
import main.data.scraper.page.category.DungeonPageScraper;
import main.entity.base.resource.Image;
import main.entity.base.resource.Page;
import main.entity.base.resource.Resource;
import main.entity.dungeon.Dungeon;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@SuppressWarnings("SimplifyOptionalCallChains")
public class DungeonPageParser extends PageParser<Dungeon> {
    
    //Constants
    
    public static final String PAGE_CATEGORY_DUNGEON = DungeonPageScraper.PAGE_CATEGORY_DUNGEON;
    
    
    //Methods
    
    @Override
    public Optional<Dungeon> tryParseData(File file) {
        return Optional.ofNullable(file)
                .map(this::parseDungeon);
    }
    
    public Dungeon parseDungeon(File page) {
        final Dungeon dungeon = new Dungeon();
        
        dungeon.page = new Page(page);
        dungeon.id = Optional.of(dungeon.page)
                .map(Resource::getName).map(e -> e.replaceAll("\\D", ""))
                .map(Integer::parseInt).orElse(null);
        
        try {
            final Document doc = Jsoup.parse(page);
            final Element container = Optional.of(doc)
                    .map(e -> e.selectFirst("body > div.container > .list-group"))
                    .orElseThrow();
            
            parseDungeonTitleSection(dungeon, container);
            parseDungeonSubDungeonSection(dungeon, container);
            
        } catch (Exception e) {
            System.err.println("Failed to parse page: " + page.getAbsolutePath());
            e.printStackTrace(System.err);
        }
        
        return dungeon;
    }
    
    private void parseDungeonTitleSection(Dungeon dungeon, Element section) {
        Optional.ofNullable(section)
                .ifPresent(titleSection -> {
                    
                    Optional.of(titleSection)
                            .map(e -> e.selectFirst(":root > li"))
                            .ifPresent(titleSectionDetails -> {
                                
                                Optional.of(titleSectionDetails)
                                        .map(e -> e.selectFirst(":root > img"))
                                        .map(e -> e.attr("src"))
                                        .ifPresent(iconUrl -> {
                                            dungeon.icon = new Image(iconUrl);
                                        });
                                
                                Optional.of(titleSectionDetails)
                                        .map(e -> e.selectFirst(":root > h1"))
                                        .map(Element::text)
                                        .ifPresent(name -> {
                                            dungeon.name = name;
                                        });
                            });
                });
    }
    
    private void parseDungeonSubDungeonSection(Dungeon dungeon, Element section) {
        Optional.ofNullable(section)
                .ifPresent(subDungeonSection -> {
                    
                    Optional.of(subDungeonSection)
                            .map(this::parseEntityGrid)
                            .ifPresent(subDungeons -> {
                                dungeon.subDungeons = subDungeons;
                            });
                });
    }
    
    
    //Getters
    
    @Override
    protected String getTypeName(boolean plural) {
        return "Dungeon" + (plural ? "s" : "");
    }
    
    @Override
    protected Class<Dungeon> getTypeClass() {
        return Dungeon.class;
    }
    
    @Override
    protected String getCategory() {
        return PAGE_CATEGORY_DUNGEON;
    }
}
