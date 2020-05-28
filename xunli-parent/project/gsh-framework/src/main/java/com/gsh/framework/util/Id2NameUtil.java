package com.gsh.framework.util;

import com.gsh.common.annotation.Id2Name;
import com.gsh.common.annotation.Id2NameClass;
import com.gsh.common.core.domain.BaseEntity;
import com.gsh.common.utils.StringUtils;
import com.gsh.system.service.ICacheService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2020/3/10 0010.
 */
@Component
public class Id2NameUtil {

    private static final Logger logger = LoggerFactory.getLogger(Id2NameUtil.class);


    @Autowired
    private ICacheService cacheService;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${gsh.framworkCacheMode}")
    private String cacheMode;


    /**
     * 给baseEntity 设置Id2Name 添加到cnNameMap里边，key:cnNameMap
     * @param baseEntity
     * @return
     */
    public BaseEntity setCnName(BaseEntity baseEntity){
        if(null == baseEntity){
            return null;
        }
        Map<String,String> cnNameMap = getCnNameMap(baseEntity);//idField name
        Map<String,Object> paraMap = baseEntity.getParams();
        paraMap.put("cnNameMap",cnNameMap);
        baseEntity.setParams(paraMap);
        return baseEntity;
    }

    /**
     * Object 属性中有Id2Name注解的，返回其中文
     * @param obj
     * @return
     */
    public Map<String,String> getCnNameMap(Object obj){
        Map<String,String> cnNameMap = new HashMap<String,String>();//idField name
        if(null == obj){
            return cnNameMap;
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        Id2Name id2NameAnnotation = null;
        Class cls = null;//需要转换id2name的类

        String id = null;
        String name = null;

        for (int i = 0; i < fields.length; i++) {
            // 属性是否含有id2name注解
            if (fields[i].isAnnotationPresent(Id2Name.class)) {
                name = "";
                try {
                    // 获取注解
                    id2NameAnnotation = fields[i].getAnnotation(Id2Name.class);
                    cls = id2NameAnnotation.cls();
                    id = getFieldValue(fields[i].getName(),obj);
                    name = getNameById(cls, id);
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }
                cnNameMap.put(fields[i].getName(),name);
            }
        }
        return cnNameMap;
    }

    /**
     * 直接获取name值
     * @param cls 类
     * @param id id
     * @return 返回类型map
     */
    public Map<String,Object> getNameByIds(String cls,String ... id){
        Map<String,Object> returnMap = new HashMap<>();
        if(null == id || StringUtils.isEmpty(cls)){
            return returnMap;
        }
        Class clazz = null;
        try {
            clazz = Class.forName(cls);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(),e);
            return returnMap;
        }
        for(int i = 0;i<id.length;i++){
            returnMap.put(id[i],getNameById(clazz, id[i]));
        }
        return returnMap;
    }

    /**
     * 直接获取name值
     * @param cls 全类名
     * @param ids ids 逗号分隔 id1,id2,id3
     * @return name1,name2,name3
     */
    public String getNameByIds(String cls,String ids){
        if(StringUtils.isEmpty(ids) || StringUtils.isEmpty(cls)){
            return "";
        }
        String[] idArray = ids.split(",");
        if(null == idArray || idArray.length == 0){
            return "";
        }
        StringBuilder names = new StringBuilder();
        Class clazz = null;
        try {
            clazz = Class.forName(cls);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(),e);
            return "";
        }
        for(int i = 0;i<idArray.length;i++){
            names.append(getNameById(clazz, idArray[i])).append(",");
        }
        names.deleteCharAt(names.length()-1);//删除最后一个逗号
        return names.toString();
    }


    /**
     * 直接获取name值
     * @param cls 配置有Id2NameClass的类
     * @param id id
     * @return
     */
    public String getNameById(Class cls, String id){
        if(null == cls || !cls.isAnnotationPresent(Id2NameClass.class) || StringUtils.isEmpty(id)){
            return "";
        }
        Id2NameClass id2NameClass = (Id2NameClass) cls.getAnnotation(Id2NameClass.class);//配置信息
        try {
            String key = cls.getName() + ".id2NameMap";//类名+.id2NameMap 作为key
            Map<String, String> map = new HashMap<String, String>();
            String name = "";
            if("redis".equals(cacheMode)){
                if(redisUtil.get(key) == null){
                    setCacheByRedis(id2NameClass, cls);
                }
                name = ((Map<String,String>)redisUtil.get(key)).get(id);
            }else if("ehcache".equals(cacheMode)){
                Cache cache = getId2NameCache();// 获取缓存对象
                if(cache.get(key) == null){
                    setCacheByEhCache(id2NameClass,cls);
                }
                name = ((Map<String,String>)cache.get(key).getObjectValue()).get(id);
            }
            return StringUtils.isEmpty(name) ? "" : name;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return "";
        }
    }

    public void setCacheByRedis(Id2NameClass id2NameClass, Class cls){
        String key = cls.getName() + ".id2NameMap";//类名+.id2NameMap 作为key
        Map<String, String> map = new HashMap<String, String>();
        for (String idCol: id2NameClass.idCol()){//多个字段的直接都put进去，
            map.putAll(list2Map(cacheService.getAllName(idCol, id2NameClass.nameCol(), id2NameClass.table(), id2NameClass.condition())));
        }
        redisUtil.set(key,map);
    }

    public void setCacheByEhCache(Id2NameClass id2NameClass, Class cls){
        String key = cls.getName() + ".id2NameMap";//类名+.id2NameMap 作为key
        Map<String, String> map = new HashMap<String, String>();
        for (String idCol: id2NameClass.idCol()){//多个字段的直接都put进去，
            map.putAll(list2Map(cacheService.getAllName(idCol, id2NameClass.nameCol(), id2NameClass.table(), id2NameClass.condition())));
        }
        Element element = new Element(key, map);
        getId2NameCache().put(element);
    }

    /**
     * 获取Id2Name ECache缓存对象
     * @return
     */
    private Cache getId2NameCache(){
        // 1. 创建缓存管理器
        CacheManager cacheManager = CacheManager.create();
        // 2. 获取缓存对象
        Cache cache = cacheManager.getCache("id2NameCache");

        if(null == cache){
            CacheConfiguration config = new CacheConfiguration();
            config.setName("id2NameCache");
            config.setMaxEntriesLocalHeap(10000);
            config.setEternal(false);
            config.setTimeToIdleSeconds(120);
            config.setTimeToLiveSeconds(120);
            config.setMaxEntriesLocalDisk(10000000);
            config.setDiskExpiryThreadIntervalSeconds(120);
            config.setMemoryStoreEvictionPolicy("LRU");
            cache = new Cache(config);
            cacheManager.addCache(cache);
        }
        return cache;
    }



    /**
     * 使用反射根据属性名称获取属性值
     *
     * @param fieldName 属性名称
     * @param o 操作对象
     * @return Object 属性值
     */

    private String getFieldValue(String fieldName, Object o){
        try{
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value==null ? "" : value.toString();
        } catch (Exception e){
            return "";
        }
    }

    /**
     * SQL 查询出来的id和name放map里边
     * @param list
     * @return
     */
    public Map<String, String> list2Map(List<Map<String, String>> list){
        Map<String, String> map = new HashMap<>();
        if(null == list || list.isEmpty()){
            return map;
        }
        for(Map<String, String> tempMap:list){
            map.put(tempMap.get("id") == null ? "" : String.valueOf(tempMap.get("id")) ,tempMap.get("name"));
        }
        return map;
    }

}
