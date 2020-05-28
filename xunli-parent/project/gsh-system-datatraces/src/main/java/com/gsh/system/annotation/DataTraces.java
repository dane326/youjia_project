package com.gsh.system.annotation;

import com.gsh.system.parser.DefaultContentParse;

import java.lang.annotation.*;

/**
 * 记录编辑详细信息的标注
 * @author lw
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DataTraces {
    /**
     * @return 获取编辑信息的解析类，目前为使用id获取，复杂的解析需要自己实现，默认不填写
     *       则使用默认解析类
     */
    Class parseclass() default DefaultContentParse.class;

    /**
     * @return 对象
     */
    Class objectClass();

    /**
     * @return 前台字段名称
     */
    String objectPkName() default "id";
    /**
     * @return 外键对象
     */
    Class foreignObjectClass() default DataTraces.class;
    /**
     * @return 外键关联id
     */
    String foreignObjectKey() default "";

    /**
     * @return 更新操作记录的详细程度: all 记录全部不为空的字段，change 记录值发生改变的字段
     */
    String updateOpDetailLevel() default "change";

    /**
     * @return 更新操作记录的详细程度：simple 记录变更的字段名称，details 记录字段值有A变为B
     */
    String updateOpContentLevel() default "simple";
}
