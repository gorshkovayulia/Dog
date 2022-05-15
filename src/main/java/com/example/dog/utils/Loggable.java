package com.example.dog.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Compile-time annotation for methods, whose execution should be logged.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface Loggable {
    enum Level {
        INFO
    }

    Level level() default Level.INFO;
}
