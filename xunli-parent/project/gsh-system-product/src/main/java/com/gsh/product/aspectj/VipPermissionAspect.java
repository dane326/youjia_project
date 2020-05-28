package com.gsh.product.aspectj;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.gsh.product.annotation.VipPermissions;
import com.gsh.product.service.ISysProductService;
import com.gsh.system.utils.FactoryUtils;

/**
 * VIP过滤处理
 * 
 * @author gsh
 */
@Aspect
@Component
public class VipPermissionAspect {

	private static final Logger logger = LoggerFactory.getLogger(VipPermissionAspect.class);
	
	@Autowired
	private ISysProductService sysProductService;

	// 配置织入点
	@Pointcut("@annotation(com.gsh.product.annotation.VipPermissions)")
	public void VipPermissionPointCut() {
	}

	@Before("VipPermissionPointCut()")
	public void doBefore(JoinPoint point) throws Throwable {
		handleVipPermissions(point);
	}

	@SuppressWarnings("unchecked")
	protected void handleVipPermissions(final JoinPoint joinPoint) {
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		if (method != null) {
			VipPermissions clVipPermission = method.getAnnotation(VipPermissions.class);
			if (clVipPermission != null) {
				if(StringUtils.isNoneBlank(clVipPermission.moduleEnname())){
					if (StringUtils.isNoneBlank(clVipPermission.paramName())) {
						Object[] objParamValues = joinPoint.getArgs();
						String[] objParamNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
						// Class[] objParameterTypes = method.getParameterTypes();
						for (int ip = 0; ip < objParamNames.length; ip++) {
							if (clVipPermission.paramName().equals(objParamNames[ip])) {
								// 获取产品配置信息
								Map<String, Object> returnMap = sysProductService.checkVipPermissionsByModuleName(FactoryUtils.getFactoryCode(), clVipPermission.moduleEnname(),Arrays.asList(clVipPermission.cfName()), (Map<String, Object>)JSONObject.toJSON(objParamValues[ip]));
								if(MapUtils.isNotEmpty(returnMap)){						
									logger.info(returnMap.toString());
								}
								return;
							}
						}
					}
				} else {
					List<String> configEnnames = new ArrayList<String>(8);
					// 先判断是否需要根据参数值来确定参数				
					if (StringUtils.isNoneBlank(clVipPermission.paramName())) {
							Object[] objParamValues = joinPoint.getArgs();
							String[] objParamNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
							// Class[] objParameterTypes = method.getParameterTypes();
							for (int ip = 0; ip < objParamNames.length; ip++) {
								if (clVipPermission.paramName().equals(objParamNames[ip])) {
									// 根据参数值获取参数配置
									String[] paramValues = clVipPermission.paramValue();
									if (ArrayUtils.isNotEmpty(paramValues)) {
										for (int iv = 0; iv < objParamNames.length; iv++) {
											if (paramValues[iv].equals(String.valueOf(objParamValues[ip]))) {
												String[] cfNames = clVipPermission.cfName();
												if (ArrayUtils.isNotEmpty(cfNames)) {
													configEnnames.add(clVipPermission.cfName()[iv]);
												}
												break;
											}
										}
									}
									break;
								}
							}
					}
					// 获取参数配置
					if (CollectionUtils.isEmpty(configEnnames)) {
						String[] cfNames = clVipPermission.cfName();
						if (ArrayUtils.isNotEmpty(cfNames)) {
							configEnnames.addAll(Arrays.asList(cfNames));
						}
					}

					if (CollectionUtils.isNotEmpty(configEnnames)) {
						// 获取产品配置信息
						Map<String, Object> returnMap = sysProductService.handleVipPermissions(FactoryUtils.getFactoryCode(), configEnnames);
						if(MapUtils.isNotEmpty(returnMap)){						
							logger.info(returnMap.toString());
						}
					}
				}				
			}
		}
	}
}
