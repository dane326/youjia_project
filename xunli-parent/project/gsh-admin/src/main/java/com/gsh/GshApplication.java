package com.gsh;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

import com.gsh.common.config.Global;

/**
 * 启动程序
 * 
 * @author gsh
 */
@EnableCaching
//@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,RedisAutoConfiguration.class,RedisRepositoriesAutoConfiguration.class})
public class GshApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(GshApplication.class, args);
        String os = System.getProperty("os.name");  
    	if(os.toLowerCase().startsWith("win")) {
    		String ip = null;
        	InetAddress address;
    		try {
    			address = InetAddress.getLocalHost();
    			ip = address.getHostAddress();
    			String path = "http://"+ip+":" + Global.getPort() + Global.getContextPath();
    			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + path);
    		} catch (UnknownHostException e) {
    			e.printStackTrace();
    		}catch (IOException e) {
                e.printStackTrace();
            }
    	}
    }

}