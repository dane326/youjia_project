package com.gsh.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;


public class GSHUtils {

//	static HashMap<String,Class> clsMap = new HashMap<String,Class>();
	static HashMap<String,Object> objMap = new HashMap<String,Object>();
	private static Class getCls(String className) {
		Class cls = null;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return cls;
	}
	public static Object getObj(String className) {
		
		try {
			if(null != objMap.get(className)) {
				return objMap.get(className);
			}else {
				Object returnObj = getCls(className).newInstance();
				objMap.put(className, returnObj);
				return returnObj;
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Object invoke(String className,String method) {
		return invoke(className,method,null);
	}
	public static Object invoke(String className,String method,Class<?>[] argsClass) {
		try {
			Object bean = getObj(className);
			Method invokeMethod  = null;
			if(null != argsClass && argsClass.length>0) {
				invokeMethod = bean.getClass().getMethod(method,argsClass);
			}else {
				invokeMethod = bean.getClass().getMethod(method);
			}
			invokeMethod.setAccessible(true);
			Object returnObj = invokeMethod.invoke(bean);
			invokeMethod.setAccessible(false);
			return returnObj;
		}catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static Object invoke(String className,String method,Class<?>[] argsClass,Object ...obj) {
		try {
			Object bean = getObj(className);
			Method invokeMethod = null;
			if(null != argsClass && argsClass.length>0) {
				invokeMethod = bean.getClass().getMethod(method,argsClass);
			}else {
				invokeMethod = bean.getClass().getMethod(method);
			}
			invokeMethod.setAccessible(true);
			Object returnObj = null;
			if(null != obj && obj.length>0) {
				returnObj = invokeMethod.invoke(bean,obj);
			}else {
				returnObj = invokeMethod.invoke(bean);
			}
			invokeMethod.setAccessible(false);
			return returnObj;
		}catch (InvocationTargetException e) {
			e.printStackTrace();
		}catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		return null;
	}
}
