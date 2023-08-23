/*
 * File:    ErrorResponse.java
 * Package: main.data.mirror.host
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.mirror.host;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import commons.access.Filesystem;

@FunctionalInterface
public interface ErrorResponse<T> extends Function<String, Supplier<T>> {
    
    //Constants
    
    File ERROR_LOG = new File("log/error.log");
    
    
    //Methods
    
    Supplier<T> respond(String errorMessage);
    
    @Override
    default Supplier<T> apply(String errorMessage) {
        return invoke(null, errorMessage);
    }
    
    
    //Static Methods
    
    static <T> Supplier<T> invoke(T response, String errorMessage) {
        return () -> {
            Optional.ofNullable(errorMessage)
                    .filter(e -> !e.isBlank())
                    .ifPresent(System.err::println);
            Filesystem.writeStringToFile(ERROR_LOG, formatErrorLog(errorMessage), true);
            return response;
        };
    }
    
    static <T> Supplier<T> invoke(String errorMessage) {
        return invoke(null, errorMessage);
    }
    
    static <T> Supplier<T> invoke() {
        return invoke(null);
    }
    
    static String formatErrorLog(String errorMessage) {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " - " + errorMessage;
    }
    
}
