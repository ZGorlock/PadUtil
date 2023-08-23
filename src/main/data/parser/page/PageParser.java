/*
 * File:    PageParser.java
 * Package: main.data.parser.page
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.parser.page;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import main.data.mirror.DataMirror;
import main.data.parser.base.Parser;
import main.entity.base.Entity;
import org.jsoup.nodes.Element;

public abstract class PageParser<T extends Entity> extends Parser<T> {
    
    //Methods
    
    @Override
    public Optional<List<File>> tryEnumerateFiles() {
        return DataMirror.getAllPageFiles(getCategory());
    }
    
    protected List<Integer> parseEntityGrid(Element node) {
        return Optional.ofNullable(node)
                .map(e -> e.select(":root > a"))
                .stream().flatMap(Collection::stream)
                .map(e -> e.attr("href")).map(e -> e.replaceAll("\\D", ""))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
    
}
