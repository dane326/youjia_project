package com.gsh.common.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Id2NameConfig注解 查询缓存
 * Created by Administrator on 2020/3/4 0004.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Id2NameClass {
    String[] idCol();// 多个字段 比如人员表 user_id,login_name 处理方式，都会put到同一个map里边
    String nameCol();
    String table();
    String condition();
}
