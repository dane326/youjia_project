package com.gsh.system.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 查询数据库用于缓存
 * 
 * @author gsh-cyq
 * @date 2020-02-19
 */
public interface CacheMapper {

    public List<Map<String, String>> getAllName(@Param("idCol") String idCol, @Param("nameCol") String nameCol, @Param("table") String table, @Param("condition") String condition);

}
