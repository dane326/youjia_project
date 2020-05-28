package com.gsh.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 程序注解配置
 *
 * @author gsh
 */
@Configuration
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
// 指定要扫描的Mapper类的包的路径
//@MapperScan("com.gsh.*.mapper")
@ComponentScan({"com.gsh","com.xunli"})
@MapperScan(value={"com.gsh.**.mapper","com.xunli.**.mapper"})
public class ApplicationConfig
{

}
