package com.gsh.quartz.service.impl;

import com.gsh.quartz.mapper.SystemJobMapper;
import com.gsh.quartz.service.ISystemJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2020/1/9 0009.
 */
@Service
public class SystemJobServiceImpl implements ISystemJobService{

    @Autowired
    private SystemJobMapper systemJobMapper;

    /**
     * 清理指定表的历史数据
     * @param tbName     表名
     * @param num        数量：比如多少年，多少月，多少天，多少小时，多少分钟，多少秒
     * @param type       单位：YEAR，MONTH，DAY，HOUR，MINUTE，SECOND
     */
    public  void  clearSystemTableData(String tbName, String num,String type){
        Map map = new HashMap();
        map.put("tbName",tbName);
        map.put("num",num);
        map.put("type",type);
        systemJobMapper.clearSystemTableData(map);
    }
}
