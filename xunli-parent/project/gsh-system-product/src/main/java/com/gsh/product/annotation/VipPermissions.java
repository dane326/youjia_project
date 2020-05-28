package com.gsh.product.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Vip注解
 * 
 * @author LCS
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VipPermissions {
	String moduleEnname() default "";

	String[] cfName() default {};
	
	String paramName() default "";
	
	String[] paramValue() default {};

	String desc() default "";	
}