package com.gsh.common.annotation;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 * 
 * @author gsh
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MapDataField
{
    public static final  String TYPE_LONGITUDE="longitude";
    public static final  String TYPE_LATITUDE="latitude";
    public static final  String TYPE_INFO="info";
    public static final  String TYPE_DEPTID="deptId";
    public static final  String TYPE_USERID="userId";
    public static final  String TYPE_PARENTID="parentId";
    public static final  String TYPE_ID="id";
    public static final  String TYPE_SORT="sort";
    /**
     * 属性类型 选择上面的常量类型
     */
    public String type() default "";
    /**
     * 是否用于划线
     */
    public String showLine() default "false";
    /**
     * 信息名称
     */
    public String name() default "";
    /**
     * 信息名称
     */
    public String detailUrl() default "";
    /**
     * 信息名称
     */
    public String openType() default "tab";


}
