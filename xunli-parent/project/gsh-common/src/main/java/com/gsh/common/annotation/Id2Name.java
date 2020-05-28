package com.gsh.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * id2name注解 切面抓取用于设置中文
 * Created by Administrator on 2020/3/4 0004.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Id2Name {
    Class cls();//取类上的 Id2NameClass 配置
}
