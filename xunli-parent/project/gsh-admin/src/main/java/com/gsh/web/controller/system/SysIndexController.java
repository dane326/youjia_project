package com.gsh.web.controller.system;

import java.util.LinkedList;
import java.util.List;

import com.gsh.system.service.ISysConfigService;
import com.gsh.system.service.ISysFactoryInfoService;
import com.xunli.config.XGlobleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import com.gsh.common.config.Global;
import com.gsh.common.core.controller.BaseController;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.SysFactoryInfo;
import com.gsh.system.domain.SysMenu;
import com.gsh.system.domain.SysUser;
import com.gsh.system.service.ISysMenuService;

import javax.servlet.http.HttpServletRequest;

/**
 * 首页 业务处理
 * 
 * @author gsh
 */
@Controller
public class SysIndexController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private ISysFactoryInfoService sysFactoryInfoService;
    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap, HttpServletRequest request)
    {
        //判断是考生还是考官登录
         String loginType =ShiroUtils.getSubject().getSession().getAttribute("loginType")==null ? "":ShiroUtils.getSubject().getSession().getAttribute("loginType").toString();
        // 取身份信息
        SysUser user = ShiroUtils.getSysUser();
        // 根据用户id取出菜单
        List<SysMenu> menus = new LinkedList<SysMenu>();
        String roleId= configService.selectConfigByKey("sys:factory:studentRoleId");
        if("1".equals(loginType)||"".equals(loginType)){
            menus=  menuService.selectMenusByUser(user);
        }else{
            if(!"".equals(roleId)){
                menus=  menuService.selectFactoryUserMenus(Long.parseLong(roleId),0);
            }

        }
        
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("sideTheme", configService.selectConfigByKey("sys.index.sideTheme"));
        mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
        mmap.put("copyrightYear", Global.getCopyrightYear());
        mmap.put("demoEnabled", Global.isDemoEnabled());
        mmap.put("licStats", XGlobleConfig.getConfig("V"));
        // 获取平台信息
        SysFactoryInfo factoryInfo = sysFactoryInfoService.selectSysFactoryInfoById(user.getDept().getFactoryCode());
        if(factoryInfo == null){
        	factoryInfo = new SysFactoryInfo();
        }
        mmap.put("factoryInfo", factoryInfo);
        
        //设置远程外链 css js
        return "index";
    }
    // 切换主题
    @GetMapping("/system/switchSkin")
    public String switchSkin(ModelMap mmap)
    {
        return "skin";
    }
    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
        mmap.put("version", Global.getVersion());
        return "main";
    }
}
