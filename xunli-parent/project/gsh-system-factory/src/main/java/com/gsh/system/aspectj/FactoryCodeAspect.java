package com.gsh.system.aspectj;

import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.gsh.common.annotation.FactoryCode;
import com.gsh.common.core.domain.BaseEntity;
import com.gsh.common.utils.StringUtils;
import com.gsh.system.domain.SysUser;
import com.gsh.system.utils.FactoryUtils;

/**
 * 数据过滤处理
 * 
 * @author gsh
 */
@Aspect
@Component
public class FactoryCodeAspect {
    /**
     * 系统编码过滤关键字
     */
    public static final String FACTORY_CODE = "factoryCode";

    // 配置织入点
    @Pointcut("@annotation(com.gsh.common.annotation.FactoryCode)")
    public void factoryCodePointCut() {
    }

    @Before("factoryCodePointCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        handleFactoryCode(point);
    }

    protected void handleFactoryCode(final JoinPoint joinPoint) {
        // 获得注解
        FactoryCode factoryCode = getAnnotationLog(joinPoint);
        if (factoryCode == null) {
            return;
        }
        // 获取当前的用户
        SysUser currentUser = FactoryUtils.getSysUser();
        if (currentUser != null) {
            // 如果是超级管理员，则不过滤数据
            if (!currentUser.isAdmin()) {
                factoryCodeFilter(joinPoint, currentUser, factoryCode);
            }
        }
    }

    /**
     * 系统编码过滤
     * 
     * @param joinPoint 切点
     * @param user 用户
     * @param factoryCode 别名
     */
    public static void factoryCodeFilter(JoinPoint joinPoint, SysUser user, FactoryCode factoryCode) {
        String factoryCodeCondi = "";
        Long fc = FactoryUtils.getFactoryCode();
        if (fc != null) {
            if (ArrayUtils.isNotEmpty(factoryCode.tableAlias())) {
                for (String alias : factoryCode.tableAlias()) {
                    factoryCodeCondi += StringUtils.format(" AND {}.factory_code = {}", alias, fc);
                }
            } else {
                factoryCodeCondi = StringUtils.format(" AND factory_code = {}", fc);
            }

        }

        if (StringUtils.isNotBlank(factoryCodeCondi)) {
            Object param = null;
            if ( joinPoint.getArgs().length > 0) {
                param = joinPoint.getArgs()[0];
            }
            if (param instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) param;
                baseEntity.getParams().put(FACTORY_CODE, factoryCodeCondi);
            }
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private FactoryCode getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(FactoryCode.class);
        }
        return null;
    }
}
