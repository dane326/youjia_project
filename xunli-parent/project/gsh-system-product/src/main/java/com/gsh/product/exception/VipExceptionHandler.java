package com.gsh.product.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.exception.VipPermissionException;
import com.gsh.common.utils.ServletUtils;

/**
 * 异常处理器
 * 
 * @author gsh
 */
//@RestControllerAdvice
public class VipExceptionHandler  {
	/**
	 * VIP异常
	 */
	//@ExceptionHandler(VipPermissionException.class)
	public Object vipPermissionException(HttpServletRequest request, VipPermissionException e) {
		if (ServletUtils.isAjaxRequest(request)) {
			return AjaxResult.error(e.getMessage());
		} else {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("errorMessage", e.getMessage());
			modelAndView.setViewName("error/vip");
			return modelAndView;
		}
	}
}
