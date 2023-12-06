/*
 * File:    ErrorResponse.java
 * Package: main.data.mirror.host
 * Author:  Zachary Gill
 * Repo:    https://github.com/ZGorlock/PadUtil
 */

package main.data.mirror.host;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;

@FunctionalInterface
public interface ErrorResponse<T> extends Function<String, Supplier<T>> {
    
    //Methods
    
    Supplier<T> respond(String errorMessage);
    
    @Override
    default Supplier<T> apply(String errorMessage) {
        return invoke(null, null, errorMessage, (Object[]) null);
    }
    
    
    //Static Methods
    
    static <T> Supplier<T> invoke(T response, Logger logger, String errorMessage, Object... messageArguments) {
        return () -> {
            Optional.ofNullable(errorMessage)
                    .filter(e -> !e.isBlank())
                    .ifPresent(message -> Optional.ofNullable(logger).ifPresentOrElse(
                            log -> Optional.ofNullable(messageArguments).ifPresentOrElse(
                                    args -> log.error(message, args),
                                    () -> log.error(message)),
                            () -> System.err.println(message)));
            return response;
        };
    }
    
    static <T> Supplier<T> invoke(Logger logger, String errorMessage, Object... messageArguments) {
        return invoke(null, logger, errorMessage, messageArguments);
    }
    
    static <T> Supplier<T> invoke(Logger logger, String errorMessage) {
        return invoke(null, logger, errorMessage, (Object[]) null);
    }
    
    static <T> Supplier<T> invoke() {
        return invoke(null, null);
    }
    
    static String formatErrorLog(String errorMessage) {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " - " + errorMessage;
    }
    
}
