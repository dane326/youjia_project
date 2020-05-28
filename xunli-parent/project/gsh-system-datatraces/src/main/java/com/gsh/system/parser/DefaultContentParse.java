package com.gsh.system.parser;

import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.spring.SpringUtils;
import com.gsh.system.domain.SysDataTracesLog;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 基础解析类
 * 单表编辑时可以直接使用id来查询
 * 如果为多表复杂逻辑，请自行编写具体实现类
 * @author lw
 */
public class DefaultContentParse implements ContentParser {
    @Override
    public Object getResult(Object fieldValue, String objectClassName) {
        List<Object[]> methodParams = new LinkedList<>();
        methodParams.add(new Object[] { fieldValue, fieldValue.getClass() });
        //首字母小写
        char chars[] = objectClassName.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        String beanName=new String(chars)+"ServiceImpl";
        String methodName = "select"+objectClassName + "ById";
        Object resultObject = null;
        try {
            resultObject =invokeMethod(beanName,methodName,methodParams);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return resultObject ;
    }



    /**
     * 调用方法
     *
     * @param beanName
     *            目标对象
     * @param methodName
     *            方法名称
     * @param methodParams
     *            方法参数
     */
    public static Object invokeMethod(String beanName, String methodName, List<Object[]> methodParams) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object bean = SpringUtils.getBean(beanName);
        if (StringUtils.isNotNull(methodParams) && methodParams.size() > 0) {
            Method method = bean.getClass().getDeclaredMethod(methodName, getMethodParamsType(methodParams));
            Object result = method.invoke(bean, getMethodParamsValue(methodParams));
            return result;
        } else {
            Method method = bean.getClass().getDeclaredMethod(methodName);
            Object result = method.invoke(bean);
            return result;
        }
    }

    /**
     * 获取参数值
     *
     * @param methodParams
     *            参数相关列表
     * @return 参数值列表
     */
    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        Object[] classs = new Object[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = os[0];
            index++;
        }
        return classs;
    }

    /**
     * 获取参数类型
     *
     * @param methodParams
     *            参数相关列表
     * @return 参数类型列表
     */
    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        Class<?>[] classs = new Class<?>[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = (Class<?>) os[1];
            index++;
        }
        return classs;
    }

}
