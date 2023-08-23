/*
 * File:    DataMirror.java
 * Package: main.data.mirror.cache
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.mirror;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import commons.access.Filesystem;
import commons.access.Project;
import commons.object.string.StringUtility;
import main.data.mirror.host.DataHost;

public final class DataMirror {
    
    //Constants
    
    public static final File MIRROR_DIR = new File(Project.DATA_DIR, ".mirror");
    
    public static final File DIR_BASE = new File(MIRROR_DIR, DataHost.HOST_BASE);
    
    public static final File DIR_STATIC = new File(MIRROR_DIR, DataHost.HOST_STATIC);
    
    
    //Static Methods
    
    public static Optional<File> getLocalFile(String sourceUrl) {
        return Optional.ofNullable(sourceUrl)
                .map(url -> url.replaceAll("^.+://", ""))
                .map(sourcePath -> new File(MIRROR_DIR, sourcePath));
    }
    
    public static Optional<File> getLocalFile(URL sourceUrl) {
        return Optional.ofNullable(sourceUrl)
                .map(e -> String.join("/", e.getHost(), e.getPath()))
                .flatMap(DataMirror::getLocalFile);
    }
    
    public static Optional<String> getSourceUrl(File localFile) {
        return Optional.ofNullable(localFile)
                .map(File::getPath).map(StringUtility::fixFileSeparators)
                .map(e -> e.replaceAll(("^.*/" + Pattern.quote(MIRROR_DIR.getName()) + "/"), ""))
                .map(e -> (DataHost.PROTOCOL + "://" + e));
    }
    
    public static Optional<String> getSourceUrl(String localFilePath) {
        return Optional.ofNullable(localFilePath)
                .map(File::new)
                .flatMap(DataMirror::getSourceUrl);
    }
    
    public static Optional<File> getPageDir(String pagePath) {
        return Optional.ofNullable(pagePath)
                .map(dir -> new File(DIR_BASE, dir));
    }
    
    public static Optional<List<File>> getAllPageFiles(String pagePath) {
        return Optional.of(getPageDir(pagePath).orElse(DIR_BASE))
                .filter(File::exists)
                .map(Filesystem::getFilesRecursively);
    }
    
    public static Optional<List<File>> getAllPageFiles() {
        return getAllPageFiles(null);
    }
    
    public static Optional<File> getPageFile(String pagePath, String pageName) {
        return Optional.of(getPageDir(pagePath).orElse(DIR_BASE))
                .flatMap(pageDir -> Optional.ofNullable(pageName)
                        .map(name -> new File(pageDir, name)));
    }
    
    public static Optional<File> getPageFile(String pageName) {
        return getPageFile(null, pageName);
    }
    
    public static Optional<File> getResourceDir(String resourcePath) {
        return Optional.ofNullable(resourcePath)
                .map(dir -> new File(DIR_STATIC, dir));
    }
    
    public static Optional<List<File>> getAllResourceFiles(String resourcePath) {
        return Optional.of(getResourceDir(resourcePath).orElse(DIR_STATIC))
                .filter(File::exists)
                .map(Filesystem::getFilesRecursively);
    }
    
    public static Optional<List<File>> getAllResourceFiles() {
        return getAllResourceFiles(null);
    }
    
    public static Optional<File> getResourceFile(String resourcePath, String resourceName) {
        return Optional.of(getResourceDir(resourcePath).orElse(DIR_STATIC))
                .flatMap(resourceDir -> Optional.ofNullable(resourceName)
                        .map(name -> new File(resourceDir, name)));
    }
    
    public static Optional<File> getResourceFile(String resourceName) {
        return getResourceFile(null, resourceName);
    }
    
    public static boolean localFileExists(File resource) {
        return Optional.ofNullable(resource)
                .map(e -> (e.exists() && !Filesystem.isEmpty(e)))
                .orElse(false);
    }
    
    public static boolean localFileNotExists(File resource) {
        return !localFileExists(resource);
    }
    
}
