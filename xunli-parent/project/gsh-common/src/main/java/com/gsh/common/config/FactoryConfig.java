package com.gsh.common.config;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.util.StringUtils;

/**
 * 接入平台配置
 * 
 * @author gsh
 */
public class FactoryConfig {
	/**
	 * 平台的factoryCode
	 * @return
	 */
	public static Long getPlatformFactoryCode() {
        return 1L;
    }

    public static Long getDeptRootId() {
        return Long.valueOf(Global.getConfig("gsh.factoryDeptRootId"));
    }

    public static Long[] getDefaultRoleIds() {
        return (Long[]) ConvertUtils.convert(StringUtils.split(Global.getConfig("gsh.factoryDefaultRoleIds")), Long.class);
    }
    
    /**
     * 开启租户系统
     * @return
     */
    public static Boolean getFactoryDisplayMode(){
    	return Boolean.valueOf(Global.getConfig("gsh.factoryDisplayMode"));
    }

    /**
     * 是否开启产品系统同步数据
     * @return
     */
    public static Boolean syncDataToProdSwitch(){
    	return Boolean.valueOf(Global.getConfig("gsh.syncDataToProd"));
    }
    
    /**
     * 与产品系统统一数据库
     * @return
     */
    public static Boolean prodDBUnifiedSwitch(){
    	return Boolean.valueOf(Global.getConfig("gsh.prodDBUnified"));
    }
    
    /**
     * 租户默认学员数
     * @return
     */
    public static String getProdDefaultAccountNum(){
    	return Global.getConfig("gsh.prodDefaultAccountNum");
    }
    
    /**
     * 租户默认在线人数
     * @return
     */
    public static String getProdDefaultOnlineUserNum(){
    	return Global.getConfig("gsh.prodDefaultOnlineUserNum");
    }
    
    /**
     * 租户默认vip级别
     * @return
     */
    public static String getProdDefaultVersion(){
    	return Global.getConfig("gsh.prodDefaultVersion");
    }
    
    /**
     * 租户默认vip级别名称
     * @return
     */
    public static String getProdDefaultVersionName(){
    	return Global.getConfig("gsh.prodDefaultVersionName");
    }
    
    /**
     * 租户vip权限天数
     * @return
     */
    public static Integer getProdDefaultVipDays(){
    	return Integer.valueOf(Global.getConfig("gsh.prodDefaultVipDays"));
    }
    
    /**
     * 支付宝支付完成后回显地址
     * 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     * @return
     */
    public static String getPayNotifyUrl(){
    	return Global.getConfig("gsh.payNotifyUrl");
    }
    
    /**
     * 支付宝支付完成回调地址
     * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     * @return
     */
    public static String getPayReturnUrl(){
    	return Global.getConfig("gsh.payReturnUrl");
    }
    
    /**
     * 生成的支付二维码地址（不带参数）
     * @return
     */
    public static String getPayQRcodeUrl(){
    	return Global.getConfig("gsh.payQRcodeUrl");
    }
    
    /**
     * 支付到付款的最大时间
     * @return
     */
    public static String getPayTimeoutExpress(){
    	return Global.getConfig("gsh.payTimeoutExpress");
    }
}
