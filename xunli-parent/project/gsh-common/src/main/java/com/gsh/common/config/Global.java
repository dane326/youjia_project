package com.gsh.common.config;

import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.YamlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局配置类
 *
 * @author gsh
 */
public class Global
{
    private static final Logger log = LoggerFactory.getLogger(Global.class);

    private static String NAME = "application.yml";

    /**
     * 当前对象实例
     */
    private static Global global;

    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = new HashMap<String, String>();

    private Global()
    {
    }

    /**
     * 静态工厂方法
     */
    public static synchronized Global getInstance()
    {
        if (global == null)
        {
            global = new Global();
        }
        return global;
    }

    /**
     * 获取配置
     */
    public static String getConfig(String key)
    {
        String value = map.get(key);
        if (value == null)
        {
            Map<?, ?> yamlMap = null;
            try
            {
                yamlMap = YamlUtil.loadYaml(NAME);
                value = String.valueOf(YamlUtil.getProperty(yamlMap, key));
                map.put(key, value != null ? value : StringUtils.EMPTY);
            }
            catch (FileNotFoundException e)
            {
                log.error("获取全局配置异常 {}", key);
            }
        }
        return value;
    }

    /**
     * 获取项目名称
     */
    public static String getName()
    {
        return StringUtils.nvl(getConfig("gsh.name"), "GSH");
    }
    
    /**
     * 获取系统端口
     */
    public static String getPort()
    {
        return StringUtils.nvl(getConfig("server.port"), "80");
    }
   


    /**
     * 获取项目版本
     */
    public static String getVersion()
    {
        return StringUtils.nvl(getConfig("gsh.version"), "4.1.0");
    }

    /**
     * 获取版权年份
     */
    public static String getCopyrightYear()
    {
        return StringUtils.nvl(getConfig("gsh.copyrightYear"), "2019");
    }

    /**
     * 实例演示开关
     */
    public static String isDemoEnabled()
    {
        return StringUtils.nvl(getConfig("gsh.demoEnabled"), "true");
    }

    /**
     * 获取ip地址开关
     */
    public static Boolean isAddressEnabled()
    {
        return Boolean.valueOf(getConfig("gsh.addressEnabled"));
    }

    /**
     * 获取文件上传路径
     */
    public static String getProfile()
    {
        return getConfig("gsh.profile");
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * 获取LOGO上传路径
     */
    public static String getLogoPath()
    {
        return getProfile() + "/logo";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }

    /**
     * 是否是使用minio的存储方式
     * @return
     */
    public static boolean isMinioFileStorageMode(){
        if("minio".equals(Global.getConfig("gsh.fileStorageMode"))){
            return true;
        }
        return false;
    }

    /**
     * 根据yaml文件来获取项目名称
     * @return
     */
	public static String getContextPath() {
		String contextPath = StringUtils.nvl(Global.getConfig("server.servlet.context-path"), "");
		if ("/".equals(contextPath)) {
			return "";
		}
		return contextPath;
	}
	
	/**
	 * 获取项目名称，不带分隔符
	 * @return
	 */
	public static String getContextPathNoSeparator(){
		return getContextPath().replace("/", "");
	}
}
