package com.gsh.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 系统编码注解
 * 
 * @author gsh
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FactoryCode {

    /**
     * 表的别名
     * 
     * @return
     */
    public String[] tableAlias() default {};

}
