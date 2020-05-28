package com.gsh.generator.util;

import java.util.Properties;
import org.apache.velocity.app.Velocity;
import com.gsh.common.constant.Constants;

/**
 * VelocityEngine工厂
 * 
 * @author gsh
 */
public class VelocityInitializer
{
    /**
     * 初始化vm方法
     */
    public static void initVelocity()
    {
        Properties p = new Properties();
        try
        {
            // 加载classpath目录下的vm文件
            p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            // 定义字符集
            p.setProperty(Velocity.ENCODING_DEFAULT, Constants.UTF8);
            p.setProperty(Velocity.OUTPUT_ENCODING, Constants.UTF8);

            //关闭运行日志以解决:org.apache.velocity.exception.VelocityException: Error initializing log: Failed to initialize an instance of org.apache.velocity.runtime.log.Log4JLogChute with the current runtime configuration.
            p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");

            // 初始化Velocity引擎，指定配置Properties
            Velocity.init(p);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
