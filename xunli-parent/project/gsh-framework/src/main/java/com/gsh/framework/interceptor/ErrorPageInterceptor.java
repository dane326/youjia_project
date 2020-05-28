package com.gsh.framework.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by wuyingkai on 2020/1/13.
 */
@Component
public class ErrorPageInterceptor extends HandlerInterceptorAdapter {
    private List<Integer> errorCodeList = Arrays.asList(404, 500);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        Random r = new Random();

        if (errorCodeList.contains(response.getStatus())) {
            response.sendRedirect(request.getContextPath()+"/error/" + response.getStatus());
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}