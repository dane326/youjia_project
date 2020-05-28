package com.gsh.web.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by wyk on 2020/1/13.
 */
@SuppressWarnings("deprecation")
@Configuration
public class WebMvcConfig  extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor( new ErrorPageInterceptor());//.addPathPatterns("/action/**", "/mine/**");默认所有
        super.addInterceptors(registry);
    }
}
