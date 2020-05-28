/**
 * shiro的redis配置
 */
package com.gsh.framework.redis;

public class ShiroKeyConstants {
	public static String REDIS_SESSION_KEY = "shiro_session::"; // session的map
	
	public static String REDIS_CACHE_KEY_PREFIX = "shiro_cache::"; // CACHE的map
	
	public static String REDIS_CACHE_AUTHOR_KEY_PREFIX = "shiro_author_cache::"; // CACHE的map
	
	public static String REDIS_CACHE_AUTHEN_KEY_PREFIX = "shiro_authen_cache::"; // CACHE的map
	
	public static String REDIS_SHIRO_CHANNEL = ""; //频道
	
	public static String REDIS_SHIRO_ONLINE_IFNO = "shiro_online_info::"; //在线用户信息
	
	public static String REDIS_SHIRO_ONLINE_LIST = "shiro_online_list::"; //在线用户列表
	
	public static String REDIS_SHIRO_SESSION_USER = "shiro_session_user::"; //session与用户关系
}