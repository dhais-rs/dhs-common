package com.dhais.annotation;

import java.lang.annotation.*;

/**
 * All rights Reserved, Designed By Fan Jun
 *
 * @author Fan Jun
 * @since 2021/2/26 16:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface DataSourceTarget {
    String value() default "default";
}
