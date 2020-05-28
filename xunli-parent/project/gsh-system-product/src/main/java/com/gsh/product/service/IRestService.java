package com.gsh.product.service;

/**
 * REST工具类接口
 * 
 * @author lcs
 * @date 2020-02-17
 */
public interface IRestService {

	/**
	 * get方法
	 * @param url
	 * @param paramObject
	 * @return
	 */
	String getRestService(String url, Object paramObject);
	
	/**
	 * get方法
	 * @param url
	 * @param paramObject
	 * @param readTimeout
	 * @param connectTimeout
	 * @return
	 */
	String getRestService(String url, Object paramObject, int readTimeout, int connectTimeout);

	/**
	 * post方法
	 * @param url
	 * @param paramObject
	 * @return
	 */
	String postRestService(String url, Object paramObject);

	/**
	 * post方法
	 * @param url
	 * @param paramObject
	 * @param readTimeout
	 * @param connectTimeout
	 * @return
	 */
	String postRestService(String url, Object paramObject, int readTimeout, int connectTimeout);
}
