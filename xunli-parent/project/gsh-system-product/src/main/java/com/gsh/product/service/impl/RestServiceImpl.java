package com.gsh.product.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.gsh.common.core.domain.BaseEntity;
import com.gsh.product.service.IRestService;

/**
 * rest服务工具类
 * 
 * @author Yi Ge Hui
 * @date 2020-02-17
 */
@Service
public class RestServiceImpl implements IRestService {
	private static final Logger logger = LoggerFactory.getLogger(RestServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	private int readTimeout = 30000;
	private int connectTimeout = 5000;

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * get方法
	 * 
	 * @param url
	 * @param paramObject
	 * @return
	 */
	@Override
	public String getRestService(String url, Object paramObject) {
		return getRestService(url, paramObject, this.readTimeout, this.connectTimeout);
	}
	
	/**
	 * get方法
	 */
	@Override
	public String getRestService(String url, Object paramObject, int readTimeout, int connectTimeout) {
		return invokeRestService(url, paramObject, HttpMethod.GET, readTimeout, connectTimeout);
	}

	/**
	 * post方法
	 * 
	 * @param url
	 * @param paramObject
	 * @return
	 */
	@Override
	public String postRestService(String url, Object paramObject) {
		return invokeRestService(url, paramObject, HttpMethod.POST, this.readTimeout, this.connectTimeout);
	}
	
	/**
	 * post方法
	 */
	@Override
	public String postRestService(String url, Object paramObject, int readTimeout, int connectTimeout) {
		return invokeRestService(url, paramObject, HttpMethod.POST, readTimeout, connectTimeout);
	}

	/**
	 * REST服务
	 * 
	 * @param url
	 * @param paramMap
	 * @param method
	 * @param readTimeout
	 *            (毫秒)
	 * @param connectTimeout
	 *            (毫秒)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String invokeRestService(String url, Object paramObject, HttpMethod method, int readTimeout, int connectTimeout) {
		String result = "{}";
		try {
			ClientHttpRequestFactory crequestFactory = restTemplate.getRequestFactory();
			if (crequestFactory == null) {
				crequestFactory = new HttpComponentsClientHttpRequestFactory();
			}
			HttpComponentsClientHttpRequestFactory requestFactory = (HttpComponentsClientHttpRequestFactory) crequestFactory;
			requestFactory.setConnectTimeout(connectTimeout);
			requestFactory.setReadTimeout(readTimeout);

			// 在配置文件中设置StringHttpMessageConverter
			// restTemplate.getMessageConverters().set(1, new
			// StringHttpMessageConverter(StandardCharsets.UTF_8));

			// HTTPBody
			String httpBody = "";
			// HTTPHeader
			HttpHeaders requestHeaders = new HttpHeaders();

			if (HttpMethod.GET == method) {
				requestHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());

				if(paramObject instanceof Map){
					Map<String, Object> paramMap = (Map<String, Object>) paramObject;
					if (MapUtils.isNotEmpty(paramMap)) {
						url += "?" + Joiner.on("&").withKeyValueSeparator("=").join(paramMap);
					}
				}				
			} else {
				MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
				requestHeaders.setContentType(type);
				requestHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
				if(paramObject instanceof Map || paramObject instanceof List || paramObject instanceof BaseEntity){
					httpBody = JSON.toJSONString(paramObject);
				}
			}

			HttpEntity<String> requestEntity = new HttpEntity<String>(httpBody, requestHeaders);

			ResponseEntity<String> response = restTemplate.exchange(url, method, requestEntity, String.class, paramObject);

			result = response.getBody();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
}