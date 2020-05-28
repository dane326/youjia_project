package com.gsh.system.service.impl;

import com.gsh.common.utils.StringUtils;
import com.gsh.system.mapper.CacheMapper;
import com.gsh.system.service.ICacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2020/3/5 0005.
 */
@Service
public class CacheServiceImpl implements ICacheService {

    @Autowired
    private CacheMapper cacheMapper;

    @Override
    public List<Map<String, String>> getAllName(String idCol, String nameCol, String table, String condition) {
        if(StringUtils.isEmpty(idCol) || StringUtils.isEmpty(nameCol) || StringUtils.isEmpty(table) ){
            return null;
        }
        condition = dealCondition(condition);
        return cacheMapper.getAllName(idCol,nameCol,table,condition);
    }

    private String dealCondition(String condition){
        if(StringUtils.isEmpty(condition)){
            return "";
        }
        if(condition.trim().toLowerCase().startsWith("and")){
            condition = " " +condition;
        }else{
            condition = " and " +condition;
        }
        return condition;
    }

}
