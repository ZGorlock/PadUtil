/*
 * File:    DataHost.java
 * Package: main.data.mirror.host
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.mirror.host;

import java.io.File;

import commons.access.Filesystem;
import commons.access.Project;

public final class DataHost {
    
    //Constants
    
    public static final File HOST_FILE = new File(Project.RESOURCES_DIR, "host.txt");
    
    public static final String PROTOCOL = "https";
    
    public static final String HOST_BASE = Filesystem.readFileToString(HOST_FILE);
    
    public static final String HOST_STATIC = HOST_BASE.replaceFirst("\\.", "-static.");
    
    public static final String URL_BASE = (PROTOCOL + "://" + HOST_BASE + "/");
    
    public static final String URL_STATIC = (PROTOCOL + "://" + HOST_STATIC + "/");
    
}
