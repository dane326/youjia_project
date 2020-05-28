package com.gsh.framework.aspectj;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import com.gsh.common.annotation.DataScope;
import com.gsh.common.core.domain.BaseEntity;
import com.gsh.common.utils.StringUtils;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.SysRole;
import com.gsh.system.domain.SysUser;

/**
 * 数据过滤处理
 * 
 * @author gsh
 */
@Aspect
@Component
public class DataScopeAspect
{
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    // 配置织入点
    @Pointcut("@annotation(com.gsh.common.annotation.DataScope)")
    public void dataScopePointCut()
    {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable
    {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint)
    {
        // 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null)
        {
            return;
        }
        // 获取当前的用户
        SysUser currentUser = ShiroUtils.getSysUser();
        if (currentUser != null)
        {
            // 如果是超级管理员，则不过滤数据
            if (!currentUser.isAdmin())
            {
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(),
                        controllerDataScope.userAlias(),controllerDataScope.deptColumnName());
            }
        }
    }

    /**
     * 数据范围过滤
     * 
     * @param joinPoint 切点
     * @param user 用户
     * @param deptAlias 部门别名
     * @param userAlias 用户别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String deptAlias, String userAlias,String deptColumnName)
    {
        StringBuilder sqlString = new StringBuilder();
        if(deptColumnName==null||"".equals(deptColumnName)){
            deptColumnName="dept_id";
        }
        for (SysRole role : user.getRoles())
        {
            String dataScope = role.getDataScope();
            if (DATA_SCOPE_ALL.equals(dataScope))
            {
                sqlString = new StringBuilder();
                break;
            }
            else if (DATA_SCOPE_CUSTOM.equals(dataScope))
            {
                if (deptAlias==null || "".equals(deptAlias.trim())) {
                    sqlString.append(StringUtils.format(
                            " OR "+deptColumnName+" IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ",
                            role.getRoleId()));
                }else{
                    sqlString.append(StringUtils.format(
                            " OR {}."+deptColumnName+" IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias,
                            role.getRoleId()));
                }

            }
            else if (DATA_SCOPE_DEPT.equals(dataScope))
            {
                if (deptAlias==null || "".equals(deptAlias.trim())) {
                    sqlString.append(StringUtils.format(" OR "+deptColumnName+" = {} ", user.getDeptId()));
                }else{
                    sqlString.append(StringUtils.format(" OR {}."+deptColumnName+" = {} ", deptAlias, user.getDeptId()));
                }
            }
            else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope))
            {
                if (deptAlias==null || "".equals(deptAlias.trim())) {
                    sqlString.append(StringUtils.format(
                            " OR "+deptColumnName+" IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                            user.getDeptId(), user.getDeptId()));
                }else{
                    sqlString.append(StringUtils.format(
                            " OR {}."+deptColumnName+" IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                            deptAlias, user.getDeptId(), user.getDeptId()));
                }


            }
            else if (DATA_SCOPE_SELF.equals(dataScope))
            {
                if (StringUtils.isNotBlank(userAlias))
                {
                    sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
                }
                else
                {
                    // 数据权限为仅本人且没有userAlias别名不查询任何数据
                    sqlString.append(" OR 1=0 ");
                }
            }
        }

        if (StringUtils.isNotBlank(sqlString.toString()))
        {
            BaseEntity baseEntity = (BaseEntity) joinPoint.getArgs()[0];
            baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint)
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}
