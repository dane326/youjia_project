package com.gsh.system.sms.utils;

import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.spring.SpringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


/**
 * 
 * @author Administrator
 *
 */
public class SmsSenderUtils {

	public static int sendSms() {

		return 1;
	}

	/**
	 * 调用任务方法
	 *
	 * @param beanName
	 *            目标对象
	 * @param methodName
	 *            方法名称
	 * @param methodParams
	 *            方法参数
	 */
	public static boolean invokeMethod(String beanName, String methodName, List<Object[]> methodParams) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object bean = SpringUtils.getBean(beanName);
		if (StringUtils.isNotNull(methodParams) && methodParams.size() > 0) {
			Method method = bean.getClass().getDeclaredMethod(methodName, getMethodParamsType(methodParams));
			Object result = method.invoke(bean, getMethodParamsValue(methodParams));
			return Boolean.valueOf(result.toString());
		} else {
			Method method = bean.getClass().getDeclaredMethod(methodName);
			Object result = method.invoke(bean);
			return Boolean.valueOf(result.toString());
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
