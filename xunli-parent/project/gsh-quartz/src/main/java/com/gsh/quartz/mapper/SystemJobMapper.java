package com.gsh.quartz.mapper;

import java.util.Map;

/**
 * Created by Administrator on 2020/1/9 0009.
 */
public interface SystemJobMapper {
    /**
     * 清理指定表的历史数据
     */
    public  void  clearSystemTableData(Map map);
}
