/*
 * File:    Page.java
 * Package: main.entity.base.resource
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.entity.base.resource;

import java.io.File;

public class Page extends Resource {
    
    //Constructors
    
    public Page() {
        super();
    }
    
    public Page(String sourceUrl) {
        super(sourceUrl);
    }
    
    public Page(File file) {
        super(file);
    }
    
}
