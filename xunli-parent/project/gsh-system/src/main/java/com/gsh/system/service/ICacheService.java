package com.gsh.system.service;

import java.util.List;
import java.util.Map;

/**
 * 缓存查询
 * Created by Administrator on 2020/3/5 0005.
 */
public interface ICacheService {

    public List<Map<String, String>> getAllName(String idCol, String nameCol, String table, String condition);

}
