package com.gsh.framework.aspectj;

import com.gsh.common.annotation.Id2NameClass;
import com.gsh.common.core.domain.BaseEntity;
import com.gsh.framework.util.Id2NameUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * id2Name 切面
 * 
 * @author gsh
 */
@Aspect
@Component
public class Id2NameAspect
{
    private static final Logger log = LoggerFactory.getLogger(Id2NameAspect.class);

    @Autowired
    private Id2NameUtil id2NameUtil;

    @Value("${gsh.framworkCacheMode}")
    private String cacheMode;

    private Map<String,String> cnNameMap = new HashMap<String,String>();

    //所有mapper里边的select和get方法
    @Pointcut("execution(* com..mapper.*.select*(..)) || execution(* com..mapper.*.get*(..)) ")
    public void selectPointCut(){
    }

    //所有mapper里边的update和insert方法 用于更新缓存
    @Pointcut("execution(* com..mapper.*.insert*(..)) || execution(* com..mapper.*.update*(..)) ")
    public void updatePointCut(){
    }

    /**
     * 在baseEntity 的para里边添加id2name信息
     */
    @AfterReturning(value = "selectPointCut()", returning="returnObj")
    public void setNameInfo(JoinPoint point, Object returnObj){
        if(returnObj == null){
            return;
        }

        if(returnObj instanceof List){
            for(Object obj : (List)returnObj){
                if(obj instanceof BaseEntity){
                    id2NameUtil.setCnName((BaseEntity)obj);
                }
            }
        }else if(returnObj instanceof BaseEntity){
            id2NameUtil.setCnName((BaseEntity)returnObj);
        }
    }

    /**
     * 更新id2name缓存
     */
    @After(value = "updatePointCut()")
    public void updateNameCache(JoinPoint point){
        // 获取方法传入参数
        Object[] params = point.getArgs();
        Object obj = params[0];
        if(null == obj || !obj.getClass().isAnnotationPresent(Id2NameClass.class)){//配置有Id2NameClass 才进行缓存更新
            return;
        }

        Id2NameClass id2NameClass = (Id2NameClass) obj.getClass().getAnnotation(Id2NameClass.class);//配置信息
        if("redis".equals(cacheMode)){
            id2NameUtil.setCacheByRedis(id2NameClass, obj.getClass());
        }else if("ehcache".equals(cacheMode)){
            id2NameUtil.setCacheByEhCache(id2NameClass,obj.getClass());
        }

    }


}
