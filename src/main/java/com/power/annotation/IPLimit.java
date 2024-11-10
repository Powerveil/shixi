package com.power.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface IPLimit {

    /**
     * 前缀key
     * @return
     */
    String prefix();

    /**
     * 次数
     * @return
     */
    int limit() default 5;

    /**
     * 窗口大小
     * @return
     */
    int windowSize() default 60;

}
