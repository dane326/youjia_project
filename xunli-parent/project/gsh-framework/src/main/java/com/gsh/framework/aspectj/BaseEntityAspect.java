package com.gsh.framework.aspectj;

import com.gsh.common.core.domain.BaseEntity;
import com.gsh.common.utils.BaseEntityUtil;
import com.gsh.common.utils.StringUtils;
import com.gsh.framework.util.Id2NameUtil;
import com.gsh.system.domain.SysDept;
import com.gsh.system.domain.SysUser;
import com.gsh.system.utils.FactoryUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 基础类新增和修改 设置公共字段
 * 
 * @author gsh
 */
@Aspect
@Component
public class BaseEntityAspect
{
    private static final Logger log = LoggerFactory.getLogger(BaseEntityAspect.class);

    @Autowired
    private Id2NameUtil id2NameUtil;

    // 新增切入点 第一个参数为BaseEntity 的子类
    @Pointcut("execution(* com..mapper.*.insert*(com.gsh.common.core.domain.BaseEntity+)) " +
           "|| execution(* com..mapper.*.save*(com.gsh.common.core.domain.BaseEntity+)) " +
           "|| execution(* com..mapper.*.add*(com.gsh.common.core.domain.BaseEntity+)) ")
    public void insertPointCut(){
    }

    // 更新切入点 第一个参数为BaseEntity 的子类
    @Pointcut("execution(* com..mapper.*.update*(com.gsh.common.core.domain.BaseEntity+))")
    public void updatePointCut(){
    }

    // 查询切入点 第一个参数为BaseEntity 的子类
    @Pointcut("execution(* com..mapper.*.select*(com.gsh.common.core.domain.BaseEntity+)) " +
           "|| execution(* com..mapper.*.get*(com.gsh.common.core.domain.BaseEntity+)) " +
           "|| execution(* com..mapper.*.check*(com.gsh.common.core.domain.BaseEntity+)) ")
    public void selectPointCut(){
    }

    /**
     * 新增 时添加默认信息
     */
    @Before("insertPointCut()")
    public void saveInsertInfo(JoinPoint joinPoint){
        BaseEntity entity = (BaseEntity)joinPoint.getArgs()[0];//传入参数
        if(null == entity){
            return;
        }
        BaseEntityUtil.setSaveInfo(entity);
    }
    /**
     * 修改 时添加默认信息
     */
    @Before("updatePointCut()")
    public void saveUpdateInfo(JoinPoint joinPoint){
        BaseEntity entity = (BaseEntity)joinPoint.getArgs()[0];//传入参数
        if(null == entity){
            return;
        }
        BaseEntityUtil.setUpdateInfo(entity);
    }

    /**
     * 查询 后添加默认信息
     * 人名和部门名称
     */
    @AfterReturning(value = "selectPointCut()", returning="returnObj")
    public void setNameInfo(JoinPoint point, Object returnObj){
        if(returnObj == null){
            return;
        }
        if(returnObj instanceof List){
            for(Object obj : (List)returnObj){
                if(!(obj instanceof BaseEntity)){
                   continue;
                }
                setBaseEntityInfo((BaseEntity)obj);
            }
        }else if(returnObj instanceof BaseEntity){
            setBaseEntityInfo((BaseEntity)returnObj);
        }

    }

    /**
     * 查询前参数设置
     * @param joinPoint
     */
    @Before("selectPointCut()")
    public void beforeSelect(JoinPoint joinPoint){
        BaseEntity entity = (BaseEntity)joinPoint.getArgs()[0];//传入参数
        if(null == entity){
            return;
        }
        try{
            entity.setFactoryCode(FactoryUtils.getFactoryCode());
        }catch (UnavailableSecurityManagerException e){
            entity.setFactoryCode(null);
        }
    }

    private void setBaseEntityInfo(BaseEntity baseEntity){
        if(baseEntity == null){
            return;
        }
        if(StringUtils.isNotEmpty(baseEntity.getCreateBy())){
            baseEntity.setCreateByName(id2NameUtil.getNameById(SysUser.class,baseEntity.getCreateBy()));
        }
        if(StringUtils.isNotEmpty(baseEntity.getUpdateBy())){
            baseEntity.setUpdateByName(id2NameUtil.getNameById(SysUser.class,baseEntity.getUpdateBy()));
        }

        if(null != baseEntity.getCreateByDeptid()){
            baseEntity.setCreateByDeptname(id2NameUtil.getNameById(SysDept.class,baseEntity.getCreateByDeptid()+""));
        }
        if(null != baseEntity.getUpdateByDeptid()){
            baseEntity.setUpdateByDeptname(id2NameUtil.getNameById(SysDept.class,baseEntity.getUpdateByDeptid()+""));
        }
    }

}
