package com.gsh.system.parser;

import com.gsh.system.domain.SysDataTracesLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * 解析接口
 *
 *
 * @author lw
 */

public interface ContentParser {

    final static Logger logger = LoggerFactory.getLogger(ContentParser.class);

    /**
     * 获取信息返回查询出的对象
     * @param fieldValue 查询条件的参数值
     * @param objectClassName 日志对象
     * @return 获得的结果
     */
    public Object getResult(Object fieldValue,  String objectClassName);
}

