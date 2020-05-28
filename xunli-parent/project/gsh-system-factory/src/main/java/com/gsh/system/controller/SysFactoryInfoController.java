package com.gsh.system.controller;

import com.gsh.common.annotation.Log;
import com.gsh.common.config.Global;
import com.gsh.common.constant.UserConstants;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.file.FileUploadUtils;
import com.gsh.common.utils.file.MinioClientUtils;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.system.domain.SysFactoryInfo;
import com.gsh.system.domain.SysUser;
import com.gsh.system.service.ISysFactoryInfoService;
import com.gsh.system.service.ISysUserService;
import com.gsh.system.utils.FactoryUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * 系统资料Controller
 * 
 * @author gsh
 * @date 2019-10-18
 */
@Controller
@RequestMapping("/system/factory/info")
public class SysFactoryInfoController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(SysFactoryInfoController.class);

    private String prefix = "system/factory/info";

    @Autowired
    private ISysFactoryInfoService sysFactoryInfoService;
    @Autowired
    private ISysUserService userService;

   /* @Autowired
    private SysPasswordService passwordService;*/

    @RequiresPermissions("system:factory:info:view")
    @GetMapping()
    public String info() {
        return prefix + "/info";
    }

    /**
     * 查询系统资料列表
     */
    @RequiresPermissions("system:factory:info:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysFactoryInfo sysFactoryInfo) {
        startPage();
        Long deptId = FactoryUtils.getSysUser().getDept().getDeptId();
        sysFactoryInfo.setCreateByDeptid(deptId);//默认查询该部门的数据.
        sysFactoryInfo.setDeleted("0");//查询未删除的数据
        List<SysFactoryInfo> list = sysFactoryInfoService.selectSysFactoryInfoList(sysFactoryInfo);
        return getDataTable(list);
    }

    /**
     * 导出系统资料列表
     */
    @RequiresPermissions("system:factory:info:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysFactoryInfo sysFactoryInfo) {
        Long deptId = FactoryUtils.getSysUser().getDept().getDeptId();
        sysFactoryInfo.setCreateByDeptid(deptId);//默认查询该部门的数据.
        sysFactoryInfo.setDeleted("0");//查询未删除的数据
        List<SysFactoryInfo> list = sysFactoryInfoService.selectSysFactoryInfoList(sysFactoryInfo);
        ExcelUtil<SysFactoryInfo> util = new ExcelUtil<SysFactoryInfo>(SysFactoryInfo.class);
        return util.exportExcel(list, "info");
    }

    /**
     * 新增系统资料
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存系统资料
     */
    @RequiresPermissions("system:factory:info:add")
    @Log(title = "系统资料", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysFactoryInfo sysFactoryInfo) {
        sysFactoryInfo.setDeleted("0");//查询未删除的数据
        return toAjax(sysFactoryInfoService.insertFactoryInfoDeptUser(sysFactoryInfo,true));
    }

    /**
     * 修改系统资料
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        SysFactoryInfo sysFactoryInfo = sysFactoryInfoService.selectSysFactoryInfoById(id);
        mmap.put("sysFactoryInfo", sysFactoryInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存系统资料
     */
    @RequiresPermissions("system:factory:info:edit")
    @Log(title = "系统资料", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysFactoryInfo sysFactoryInfo) {
        return toAjax(sysFactoryInfoService.updateSysFactoryInfo(sysFactoryInfo));
    }

    /**
     * 修改保存系统资料
     */
    @Log(title = "系统资料", businessType = BusinessType.UPDATE)
    @PostMapping("/key")
    @ResponseBody
    public AjaxResult key(SysFactoryInfo sysFactoryInfo) {
        sysFactoryInfo.setSecretKey(FactoryUtils.uuid());
        sysFactoryInfoService.updateSysFactoryInfo(sysFactoryInfo);
        return success(sysFactoryInfo.getSecretKey());
    }

    /**
     * 删除系统资料
     */
    @RequiresPermissions("system:factory:info:remove")
    @Log(title = "系统资料", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysFactoryInfoService.logicDeleteSysFactoryInfoByIds(ids));
    }

    /**
     * 显示系统资料
     */
    @RequiresPermissions("system:factory:info:detail")
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap mmap) {
        SysFactoryInfo sysFactoryInfo = sysFactoryInfoService.selectSysFactoryInfoById(id);
        mmap.put("sysFactoryInfo", sysFactoryInfo);
        return prefix + "/detail";
    }

    /**
     * 显示系统资料
     */
    @RequiresPermissions("system:factory:info:detail")
    @GetMapping("/detail")
    public String detail(ModelMap mmap) {

        if (1== FactoryUtils.getSysUser().getUserId()) {//admin沒有厂家code
            Long deptId = FactoryUtils.getSysUser().getDept().getDeptId();
            SysFactoryInfo sysFactoryInfo=new SysFactoryInfo();
            sysFactoryInfo.setCreateByDeptid(deptId);//默认查询该部门的数据.
            sysFactoryInfo.setDeleted("0");//查询未删除的数据
            List<SysFactoryInfo> list = sysFactoryInfoService.selectSysFactoryInfoList(sysFactoryInfo);
            mmap.put("sysFactoryInfo", list.get(0));
        }else{
            Long id = FactoryUtils.getFactoryCode();
            SysFactoryInfo sysFactoryInfo = sysFactoryInfoService.selectSysFactoryInfoById(id);
            mmap.put("sysFactoryInfo", sysFactoryInfo);
        }

        return prefix + "/detail";
    }

    @GetMapping("/{id}/{field}/avatar")
    public String avatar(@PathVariable("id") Long id, @PathVariable("field") String field, ModelMap mmap) {
        SysFactoryInfo sysFactoryInfo = sysFactoryInfoService.selectSysFactoryInfoById(id);
        mmap.put("id", id);
        mmap.put("field", field);

        if ("feLogo".equals(field)) {
            mmap.put("avatar", sysFactoryInfo.getFeLogo());
        } else if ("platformLogo".equals(field)) {
            mmap.put("avatar", sysFactoryInfo.getPlatformLogo());
        } else if ("sysLogo".equals(field)) {
            mmap.put("avatar", sysFactoryInfo.getSysLogo());
        }        
        
        return prefix + "/avatar";
    }

    /**
     * 显示头像
     */
    @GetMapping("show/avatar/{id}/{field}")
    public void showAvatar(@PathVariable("id") Long id, @PathVariable("field") String field, HttpServletResponse response)
    {
        try {
            String avatar = MinioClientUtils.getInstance().getFactoryLogoFileName("factory", id, field);
            InputStream inputStream = MinioClientUtils.getInstance().getObject(avatar);
            MinioClientUtils.getInstance().writeBytes(inputStream, response.getOutputStream());
        } catch (Exception e) {
            logger.error("获取图片失败", e);
        }
    }

    /**
     * 保存图片
     */
    @Log(title = "系统资料", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/{field}/updateAvatar")
    @ResponseBody
    public AjaxResult updateAvatar(@PathVariable("id") Long id, @PathVariable("field") String field, @RequestParam("avatarfile") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String avatar = "";
                if(Global.isMinioFileStorageMode()) {
                    avatar = MinioClientUtils.getInstance().uploadFactoryLogo(id, field, file);
                    avatar = String.format("/system/factory/info/show/avatar/%s/%s", String.valueOf(id),field);
                }else{
                    avatar = FileUploadUtils.upload(Global.getLogoPath(), file);
                }
                SysFactoryInfo sysFactoryInfo = new SysFactoryInfo();
                sysFactoryInfo.setId(id);
                if ("feLogo".equals(field)) {
                    sysFactoryInfo.setFeLogo(avatar);
                } else if ("platformLogo".equals(field)) {
                    sysFactoryInfo.setPlatformLogo(avatar);
                } else if ("sysLogo".equals(field)) {
                    sysFactoryInfo.setSysLogo(avatar);
                }
                if (sysFactoryInfoService.updateSysFactoryInfo(sysFactoryInfo) > 0) {
                    return success(avatar);
                }
            }
            return error();
        } catch (Exception e) {
            log.error("修改图片失败！", e);
            return error(e.getMessage());
        }
    }

    @PostMapping("/register")
    @ResponseBody
    public AjaxResult ajaxRegister(@Validated SysUser user, HttpServletRequest request)
    {
    	/*String code = request.getParameter("validateCode");
        String sessionCode = (String) StringUtils.nvl(request.getSession().getAttribute("validateCode"), "123456");
        //单机版本去掉验证码注册账号
        if(StringUtils.isEmpty(code) || !code.equals(sessionCode)){
            return error("验证码错误");
        }*/

        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName())))
        {
            return error("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
        }
        else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return error("新增用户'" + user.getLoginName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return error("新增用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
        }
        return toAjax(sysFactoryInfoService.insertFactoryInfoDeptUser(user, true));
    }

    @PostMapping("/resetPassword")
    @ResponseBody
    public AjaxResult resetPassword(@Validated SysUser user, HttpServletRequest request)
    {
        String code = request.getParameter("validateCode");
        String sessionCode = (String) StringUtils.nvl(request.getSession().getAttribute("validateCode"), "123456");
        if(StringUtils.isEmpty(code) || !code.equals(sessionCode)){
            return error("验证码错误");
        }
        SysUser oldUser=userService.selectUserByPhoneNumber(user.getPhonenumber());
        //为用户赋予默认角色及权限
        oldUser.setSalt(FactoryUtils.randomSalt());
        oldUser.setPassword(FactoryUtils.encryptPassword(oldUser.getLoginName(), user.getPassword(), oldUser.getSalt()));
        return toAjax(userService.updateUserInfo(oldUser));
    }
}
