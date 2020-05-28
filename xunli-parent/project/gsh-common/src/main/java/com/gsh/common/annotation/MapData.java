package com.gsh.common.annotation;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 * 
 * @author gsh
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MapData
{
    public static final  String TYPE_ADD="add";
    public static final  String TYPE_UPDATE="update";
    public static final  String TYPE_DELETE="delete";
    /**
     * 操作类型
     */
    public String operateType() default TYPE_ADD;
    /**
     * 是否有权限过滤
     */
    public String permission() default "false";

}
