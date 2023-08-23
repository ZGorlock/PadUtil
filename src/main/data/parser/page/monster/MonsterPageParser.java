/*
 * File:    MonsterPageParser.java
 * Package: main.data.parser.page.monster
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.parser.page.monster;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import main.data.parser.page.PageParser;
import main.data.scraper.page.category.MonsterPageScraper;
import main.entity.base.resource.Image;
import main.entity.base.resource.Page;
import main.entity.monster.Monster;
import main.entity.monster.awakening.Awakening;
import main.entity.monster.detail.Series;
import main.entity.monster.detail.Type;
import main.entity.monster.evolution.EvoTree;
import main.entity.monster.evolution.Evolution;
import main.entity.monster.skill.ActiveSkill;
import main.entity.monster.skill.LeaderSkill;
import main.entity.monster.skill.SkillTag;
import main.entity.monster.stat.StatBlock;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@SuppressWarnings("SimplifyOptionalCallChains")
public class MonsterPageParser extends PageParser<Monster> {
    
    //Constants
    
    public static final String PAGE_CATEGORY_MONSTER = MonsterPageScraper.PAGE_CATEGORY_MONSTER;
    
    
    //Methods
    
    @Override
    public Optional<Monster> tryParseData(File file) {
        return Optional.ofNullable(file)
                .map(this::parseMonster);
    }
    
    public Monster parseMonster(File page) {
        final Monster monster = new Monster();
        
        monster.page = new Page(page);
        
        try {
            final Document doc = Jsoup.parse(page);
            final Elements container = Optional.of(doc)
                    .map(e -> e.selectFirst("body > div.container"))
                    .map(e -> e.select(":root > .list-group > .list-group-item"))
                    .orElseThrow();
            
            parseMonsterImageSection(monster, container.get(0));
            parseMonsterTitleSection(monster, container.get(1));
            parseMonsterSkillSection(monster, container.get(2));
            parseMonsterStatsSection(monster, container.get(3));
            
        } catch (Exception e) {
            System.err.println("Failed to parse page: " + page.getAbsolutePath());
            e.printStackTrace(System.err);
        }
        
        return monster;
    }
    
    private void parseMonsterImageSection(Monster monster, Element section) {
        Optional.ofNullable(section)
                .ifPresent(imageSection -> {
                    
                    Optional.of(imageSection)
                            .map(e -> e.selectFirst(":root > img"))
                            .map(e -> e.attr("src"))
                            .ifPresent(iconUrl -> {
                                monster.icon = new Image(iconUrl);
                            });
                });
    }
    
    private void parseMonsterTitleSection(Monster monster, Element section) {
        Optional.ofNullable(section)
                .ifPresent(titleSection -> {
                    
                    Optional.of(titleSection)
                            .map(e -> e.selectFirst(":root > img.monster-icon"))
                            .map(e -> e.attr("src"))
                            .ifPresent(imageUrl -> {
                                monster.image = new Image(imageUrl);
                            });
                    
                    Optional.of(titleSection)
                            .map(e -> e.selectFirst(":root > div.flex-column"))
                            .ifPresent(titleInfo -> {
                                
                                Optional.of(titleInfo)
                                        .map(e -> e.selectFirst(":root > h1.monster-name"))
                                        .map(Element::text)
                                        .ifPresent(name -> {
                                            monster.name = name;
                                        });
                                
                                Optional.of(titleInfo)
                                        .map(e -> e.selectFirst(":root > div.flex-wrap"))
                                        .ifPresent(titleDetails -> {
                                            
                                            Optional.of(titleDetails)
                                                    .map(e -> e.selectFirst(":root > span:nth-of-type(1)"))
                                                    .map(Element::text)
                                                    .map(Integer::parseInt)
                                                    .ifPresent(id -> {
                                                        monster.id = id;
                                                    });
                                            
                                            Optional.of(titleDetails)
                                                    .map(e -> e.selectFirst(":root > span:nth-of-type(2)"))
                                                    .map(Element::text).map(e -> e.replaceAll("(?i)\\s*stars?", ""))
                                                    .map(Integer::parseInt)
                                                    .ifPresent(stars -> {
                                                        monster.stars = stars;
                                                    });
                                            
                                            Optional.of(titleDetails)
                                                    .map(e -> e.select(":root > img.type-icon"))
                                                    .map(e -> e.stream()
                                                            .map(e2 -> e2.attr("src"))
                                                            .map(Type::extractIdFromIcon)
                                                            //.map(Type::lookupEntity)
                                                            .collect(Collectors.toList()))
                                                    .ifPresent(type -> {
                                                        monster.type = type;
                                                    });
                                            
                                            Optional.of(titleDetails)
                                                    .map(e -> e.select(":root > a[href*=series]"))
                                                    .map(e -> e.stream()
                                                            .map(e2 -> e2.attr("href"))
                                                            .map(Series::extractId)
                                                            //.map(Series::lookupEntity)
                                                            .collect(Collectors.toList()))
                                                    .ifPresent(series -> {
                                                        monster.series = series;
                                                    });
                                        });
                            });
                });
    }
    
    private void parseMonsterSkillSection(Monster monster, Element section) {
        Optional.ofNullable(section)
                .ifPresent(skillSection -> {
                    
                    Optional.of(skillSection)
                            .map(e -> e.selectFirst(":root > div.row > div:nth-of-type(1)"))
                            .ifPresent(skillsInfo -> {
                                
                                Optional.of(skillsInfo)
                                        .map(e -> e.selectFirst(":root > small:matchesWholeText(Awoken Skills) + div"))
                                        .flatMap(this::parseAwakeningList)
                                        .ifPresent(awakenings -> {
                                            monster.awakenings = awakenings;
                                        });
                                
                                Optional.of(skillsInfo)
                                        .map(e -> e.selectFirst(":root > small:contains(Super Awoken) + div"))
                                        .flatMap(this::parseAwakeningList)
                                        .ifPresent(superAwakenings -> {
                                            monster.superAwakenings = superAwakenings;
                                        });
                                
                                Optional.of(skillsInfo)
                                        .map(e -> e.selectFirst(":root > small:contains(Applicable Latent) + div"))
                                        .flatMap(this::parseAwakeningList)
                                        .ifPresent(applicableKillerLatents -> {
                                            monster.applicableKillerLatents = applicableKillerLatents;
                                        });
                                
                                Optional.of(skillsInfo)
                                        .map(e -> e.selectFirst(":root > div.flex-wrap:last-of-type"))
                                        .ifPresent(pointDetails -> {
                                            
                                            Optional.of(pointDetails)
                                                    .map(e -> e.select(":root > div > small:contains(Extra Latent) + div"))
                                                    .map(e -> !e.isEmpty())
                                                    .ifPresent(extraLatents -> {
                                                        monster.extraLatents = extraLatents;
                                                    });
                                            
                                            Optional.of(pointDetails)
                                                    .map(e -> e.selectFirst(":root > div > small:contains(Monster Point) + div"))
                                                    .map(Element::text).map(e -> e.replaceAll("\\D", ""))
                                                    .map(Integer::parseInt)
                                                    .ifPresent(monsterPoints -> {
                                                        monster.monsterPoints = monsterPoints;
                                                    });
                                            
                                            Optional.of(pointDetails)
                                                    .map(e -> e.selectFirst(":root > div > small:contains(Max. Exp.) + div"))
                                                    .map(Element::text).map(e -> e.replaceAll("\\D", ""))
                                                    .map(Long::parseLong)
                                                    .ifPresent(maxExp -> {
                                                        monster.maxExp = maxExp;
                                                    });
                                            
                                            Optional.of(pointDetails)
                                                    .map(e -> e.selectFirst(":root > div > small:contains(Cost) + div"))
                                                    .map(Element::text).map(e -> e.replaceAll("\\D", ""))
                                                    .map(Integer::parseInt)
                                                    .ifPresent(cost -> {
                                                        monster.cost = cost;
                                                    });
                                        });
                            });
                    
                    Optional.of(skillSection)
                            .map(e -> e.selectFirst(":root > div.row > div:nth-of-type(2)"))
                            .ifPresent(skillsDetails -> {
                                
                                Optional.of(skillsDetails)
                                        .flatMap(this::parseActiveSkill)
                                        .filter(e -> (e.name != null))
                                        .ifPresent(activeSkill -> {
                                            monster.activeSkill = activeSkill;
                                        });
                                
                                Optional.of(skillsDetails)
                                        .flatMap(this::parseLeaderSkill)
                                        .filter(e -> (e.name != null))
                                        .ifPresent(leaderSkill -> {
                                            monster.leaderSkill = leaderSkill;
                                        });
                            });
                });
    }
    
    private void parseMonsterStatsSection(Monster monster, Element section) {
        Optional.ofNullable(section)
                .ifPresent(statsSection -> {
                    
                    Optional.of(statsSection)
                            .map(e -> e.selectFirst(":root > div.row > div > table"))
                            .map(statTable -> statTable.select(":root > tbody > tr:not([hidden]):not(:has(div))"))
                            .map(statTable -> statTable.stream()
                                    .map(statSet -> statSet.select(":root > td"))
                                    .map(statSet -> statSet.stream()
                                            .map(Element::ownText).map(Integer::parseInt)
                                            .collect(Collectors.toList()))
                                    .map(StatBlock::new)
                                    .collect(Collectors.toList()))
                            .ifPresent(statBlocks -> {
                                monster.statBlocks = statBlocks;
                            });
                    
                    Optional.of(statsSection)
                            .map(e -> e.selectFirst(":root > div.row > div > small:contains(Evolution) + div.tree"))
                            .map(this::parseMonsterEvoTree)
                            .map(EvoTree::new)
                            .ifPresent(evoTree -> {
                                monster.evoTree = evoTree;
                            });
                    
                    Optional.of(statsSection)
                            .map(e -> e.selectFirst(":root > div.row > div > small:contains(Same Skill) + div"))
                            .map(this::parseEntityGrid)
                            .ifPresent(sameActiveSkillMonsters -> {
                                monster.sameActiveSkillMonsters = sameActiveSkillMonsters;
                            });
                    
                    Optional.of(statsSection)
                            .map(e -> e.selectFirst(":root > div.row > div > small:contains(Similar Active Skill) + div"))
                            .map(this::parseEntityGrid)
                            .ifPresent(similarSkillList -> {
                                monster.similarActiveSkillMonsters = similarSkillList;
                            });
                    
                    Optional.of(statsSection)
                            .map(e -> e.selectFirst(":root > div.row > div > small + div.pt-1"))
                            .map(this::parseEntityGrid)
                            .ifPresent(sameSeriesMonsters -> {
                                monster.sameSeriesMonsters = sameSeriesMonsters;
                            });
                    
                    Optional.of(statsSection)
                            .map(e -> e.selectFirst(":root > div.row > div > small:contains(Drops) + div"))
                            .map(this::parseEntityGrid)
                            .ifPresent(dropLocations -> {
                                monster.sameActiveSkillMonsters = dropLocations;
                            });
                });
    }
    
    private Optional<List<Integer>> parseAwakeningList(Element node) {
        return Optional.ofNullable(node)
                .map(e -> e.select(":root > img.smaller-awoken-icon"))
                .map(e -> e.stream()
                        .map(e2 -> e2.attr("src"))
                        .map(Awakening::extractIdFromIcon)
                        //.map(Awakening::lookupEntity)
                        .collect(Collectors.toList()));
    }
    
    private Optional<ActiveSkill> parseActiveSkill(Element node) {
        return Optional.ofNullable(node)
                .map(e -> e.select(":root > div.flex-row.justify-content-between > div.mr-2"))
                .filter(ArrayList::isEmpty)
                .map(e -> parseBasicActiveSkill(node))
                .orElseGet(() -> parseEvolvingActiveSkill(node));
    }
    
    private Optional<ActiveSkill> parseBasicActiveSkill(Element node) {
        return Optional.ofNullable(node)
                .map(activeSkillDetails -> {
                    final ActiveSkill activeSkill = new ActiveSkill();
                    
                    Optional.of(activeSkillDetails)
                            .map(e -> e.selectFirst(":root > div > small:contains(Active Skill)"))
                            .map(Element::text).map(e -> e.replaceAll("Active Skill\\s*-\\s*", ""))
                            .ifPresent(name -> {
                                activeSkill.name = name;
                            });
                    
                    Optional.of(activeSkillDetails)
                            .map(e -> e.selectFirst(":root > div > span:has(span.badge)"))
                            .ifPresent(countdownDetails -> {
                                
                                Optional.of(countdownDetails)
                                        .map(e -> e.selectFirst(":root > span.badge-steelblue"))
                                        .map(Element::text).map(e -> e.replaceAll("(?i)LV\\.\\s*1\\s*CD:\\s*(\\d+)", "$1"))
                                        .map(Integer::parseInt)
                                        .ifPresent(baseCountdown -> {
                                            activeSkill.baseCountdown = baseCountdown;
                                        });
                                
                                Optional.of(countdownDetails)
                                        .map(e -> e.selectFirst(":root > span.badge-danger"))
                                        .map(Element::text).map(e -> e.replaceAll("(?i)LV\\.\\s*MAX\\s*CD:\\s*(\\d+)", "$1"))
                                        .map(Integer::parseInt)
                                        .ifPresent(minCountdown -> {
                                            activeSkill.minCountdown = minCountdown;
                                        });
                                
                                Optional.ofNullable(activeSkill.baseCountdown)
                                        .map(e -> (e - Optional.ofNullable(activeSkill.minCountdown).orElse(0)))
                                        .ifPresent(maxLevel -> {
                                            activeSkill.maxLevel = maxLevel;
                                        });
                            });
                    
                    Optional.of(activeSkillDetails)
                            .map(e -> e.selectFirst(":root > div > div.active-skill-desc"))
                            .map(Element::ownText)
                            .ifPresent(description -> {
                                activeSkill.description = description;
                            });
                    
                    Optional.of(activeSkillDetails)
                            .map(e -> e.selectFirst(":root > div > div.align-items-end > a"))
                            .map(e -> e.attr("href")).map(e -> e.replaceAll("\\D", ""))
                            .map(Integer::parseInt)
                            .ifPresent(transformTo -> {
                                activeSkill.transformTo = transformTo;
                            });
                    
                    Optional.of(activeSkillDetails)
                            .map(e -> e.select(":root > div > div.active-skill-desc > div > a.badge[href*=keywords]"))
                            .map(e -> e.stream()
                                    .map(e2 -> new SkillTag(e2.text(), e2.attr("href")))
                                    .collect(Collectors.toList()))
                            .ifPresent(tags -> {
                                activeSkill.tags = tags;
                            });
                    
                    return activeSkill;
                });
    }
    
    private Optional<ActiveSkill> parseEvolvingActiveSkill(Element node) {
        return Optional.ofNullable(node)
                .map(activeSkillDetails -> {
                    final ActiveSkill activeSkill = new ActiveSkill();
                    
                    Optional.of(activeSkillDetails)
                            .map(e -> e.selectFirst(":root > div > small:contains(Active Skill)"))
                            .map(Element::text).map(e -> e.replaceAll("Active Skill\\s*-\\s*", ""))
                            .ifPresent(name -> {
                                activeSkill.name = name;
                            });
                    
                    Optional.of(activeSkillDetails)
                            .map(e -> e.selectFirst(":root > div.flex-row.justify-content-between:nth-of-type(2) > div.mr-2"))
                            .ifPresent(baseActiveSkillDetails -> {
                                
                                Optional.of(baseActiveSkillDetails)
                                        .map(e -> e.selectFirst(":root > span:has(span.badge)"))
                                        .ifPresent(countdownDetails -> {
                                            
                                            Optional.of(countdownDetails)
                                                    .map(e -> e.selectFirst(":root > span.badge-steelblue"))
                                                    .map(Element::text).map(e -> e.replaceAll("(?i)LV\\.\\s*1\\s*CD:\\s*(\\d+)", "$1"))
                                                    .map(Integer::parseInt)
                                                    .ifPresent(baseCountdown -> {
                                                        activeSkill.baseCountdown = baseCountdown;
                                                    });
                                            
                                            Optional.of(countdownDetails)
                                                    .map(e -> e.selectFirst(":root > span.badge-danger"))
                                                    .map(Element::text).map(e -> e.replaceAll("(?i)LV\\.\\s*MAX\\s*CD:\\s*(\\d+)", "$1"))
                                                    .map(Integer::parseInt)
                                                    .ifPresent(minCountdown -> {
                                                        activeSkill.minCountdown = minCountdown;
                                                    });
                                            
                                            Optional.ofNullable(activeSkill.baseCountdown)
                                                    .map(e -> (e - Optional.ofNullable(activeSkill.minCountdown).orElse(0)))
                                                    .ifPresent(maxLevel -> {
                                                        activeSkill.maxLevel = maxLevel;
                                                    });
                                        });
                                
                                Optional.of(baseActiveSkillDetails)
                                        .map(e -> e.selectFirst(":root > p.my-0"))
                                        .map(Element::ownText)
                                        .ifPresent(description -> {
                                            activeSkill.description = description;
                                        });
                                
                                Optional.of(baseActiveSkillDetails)
                                        .map(e -> e.select(":root > div > a.badge[href*=keywords]"))
                                        .map(e -> e.stream()
                                                .map(e2 -> new SkillTag(e2.text(), e2.attr("href")))
                                                .collect(Collectors.toList()))
                                        .ifPresent(tags -> {
                                            activeSkill.tags = tags;
                                        });
                            });
                    
                    Optional.of(activeSkillDetails)
                            .map(e -> e.selectFirst(":root > div.flex-row.justify-content-between:nth-of-type(3) > div.mr-2"))
                            .ifPresent(baseActiveSkillDetails -> {
                                final ActiveSkill evolvedSkill = new ActiveSkill();
                                
                                Optional.of(baseActiveSkillDetails)
                                        .map(e -> e.selectFirst(":root > span:has(span.badge)"))
                                        .ifPresent(countdownDetails -> {
                                            
                                            Optional.of(countdownDetails)
                                                    .map(e -> e.selectFirst(":root > span.badge-steelblue"))
                                                    .map(Element::text).map(e -> e.replaceAll("(?i)LV\\.\\s*1\\s*CD:\\s*(\\d+)", "$1"))
                                                    .map(Integer::parseInt)
                                                    .ifPresent(baseCountdown -> {
                                                        evolvedSkill.baseCountdown = baseCountdown;
                                                    });
                                            
                                            Optional.of(countdownDetails)
                                                    .map(e -> e.selectFirst(":root > span.badge-danger"))
                                                    .map(Element::text).map(e -> e.replaceAll("(?i).*CD:\\s*(\\d+)", "$1"))
                                                    .map(Integer::parseInt)
                                                    .ifPresent(minCountdown -> {
                                                        evolvedSkill.minCountdown = minCountdown;
                                                    });
                                            
                                            Optional.ofNullable(evolvedSkill.baseCountdown)
                                                    .map(e -> (e - Optional.ofNullable(evolvedSkill.minCountdown).orElse(0)))
                                                    .ifPresent(maxLevel -> {
                                                        evolvedSkill.maxLevel = maxLevel;
                                                    });
                                        });
                                
                                Optional.of(baseActiveSkillDetails)
                                        .map(e -> e.selectFirst(":root > p.my-0"))
                                        .map(Element::ownText)
                                        .ifPresent(description -> {
                                            evolvedSkill.description = description;
                                        });
                                
                                Optional.of(baseActiveSkillDetails)
                                        .map(e -> e.select(":root > div > a.badge[href*=keywords]"))
                                        .map(e -> e.stream()
                                                .map(e2 -> new SkillTag(e2.text(), e2.attr("href")))
                                                .collect(Collectors.toList()))
                                        .ifPresent(tags -> {
                                            evolvedSkill.tags = tags;
                                        });
                                
                                activeSkill.evolvedSkill = evolvedSkill;
                            });
                    
                    return activeSkill;
                });
    }
    
    private Optional<LeaderSkill> parseLeaderSkill(Element node) {
        return Optional.ofNullable(node)
                .map(leaderSkillDetails -> {
                    final LeaderSkill leaderSkill = new LeaderSkill();
                    
                    Optional.of(leaderSkillDetails)
                            .map(e -> e.selectFirst(":root > small:contains(Leader Skill)"))
                            .map(Element::text).map(e -> e.replaceAll("Leader Skill\\s*-\\s*", ""))
                            .ifPresent(name -> {
                                leaderSkill.name = name;
                            });
                    
                    Optional.of(leaderSkillDetails)
                            .map(e -> e.selectFirst(":root > small:contains(Leader Skill) + div > p:first-of-type"))
                            .map(Element::ownText)
                            .ifPresent(description -> {
                                leaderSkill.description = description;
                            });
                    
                    Optional.of(leaderSkillDetails)
                            .map(e -> e.selectFirst(":root > div > p:contains(Effective HP Ratio)"))
                            .map(Element::ownText).map(e -> e.replaceAll("[^\\d.]", ""))
                            .map(Double::parseDouble)
                            .ifPresent(effectiveHpRatio -> {
                                leaderSkill.effectiveHpRatio = effectiveHpRatio;
                            });
                    
                    Optional.of(leaderSkillDetails)
                            .map(e -> e.select(":root > div.pt-2 > a.badge[href*=keywords]"))
                            .map(e -> e.stream()
                                    .map(e2 -> new SkillTag(e2.text(), e2.attr("href")))
                                    .collect(Collectors.toList()))
                            .ifPresent(tags -> {
                                leaderSkill.tags = tags;
                            });
                    
                    return leaderSkill;
                });
    }
    
    private List<Evolution> parseMonsterEvoTree(Element node) {
        return parseMonsterEvoTree(node, null);
    }
    
    private List<Evolution> parseMonsterEvoTree(Element node, Integer parent) {
        final List<Evolution> evolutions = new ArrayList<>();
        
        final Evolution evo = new Evolution();
        evo.base = parent;
        
        Optional.ofNullable(node)
                .map(Element::children).orElseGet(Elements::new)
                .forEach(childNode -> {
                    switch (childNode.tag().normalName()) {
                        case "a":
                            evo.id = Optional.of(childNode)
                                    .map(e -> e.attr("href")).map(e -> e.replaceAll("\\D", ""))
                                    .map(Integer::parseInt).orElse(null);
                            evolutions.add(evo);
                            break;
                        case "div":
                            evo.materials = childNode.select(":root.materials > a").stream()
                                    .map(e -> e.attr("href")).map(e -> e.replaceAll("\\D", ""))
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList());
                            break;
                        case "li":
                            evolutions.addAll(parseMonsterEvoTree(childNode, evo.base));
                            break;
                        case "ul":
                            evolutions.addAll(parseMonsterEvoTree(childNode, evo.id));
                            break;
                    }
                });
        
        return evolutions;
    }
    
    
    //Getters
    
    @Override
    protected String getTypeName(boolean plural) {
        return "Monster" + (plural ? "s" : "");
    }
    
    @Override
    protected Class<Monster> getTypeClass() {
        return Monster.class;
    }
    
    @Override
    protected String getCategory() {
        return PAGE_CATEGORY_MONSTER;
    }
    
}
