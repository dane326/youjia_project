package com.gsh.web.controller.system;

import com.gsh.common.annotation.Log;
import com.gsh.common.config.Global;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.file.FileUploadUtils;
import com.gsh.common.utils.file.MinioClientUtils;
import com.gsh.framework.shiro.service.SysPasswordService;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.SysUser;
import com.gsh.system.service.ISysUserService;
import com.gsh.system.utils.FactoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * 个人信息 业务处理
 * 
 * @author gsh
 */
@Controller
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(SysProfileController.class);

    private String prefix = "system/user/profile";

    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private SysPasswordService passwordService;

    @Value("${server.servlet.context-path}")
    private String context;

    /**
     * 个人信息
     */
    @GetMapping()
    public String profile(ModelMap mmap)
    {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user", user);
        mmap.put("roleGroup", userService.selectUserRoleGroup(user.getUserId()));
        mmap.put("postGroup", userService.selectUserPostGroup(user.getUserId()));
        return prefix + "/profile";
    }

    @GetMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password)
    {
        SysUser user = ShiroUtils.getSysUser();
        if (passwordService.matches(user, password))
        {
            return true;
        }
        return false;
    }

    @GetMapping("/resetPwd")
    public String resetPwd(ModelMap mmap)
    {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user", userService.selectUserById(user.getUserId()));
        return prefix + "/resetPwd";
    }

    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwd(String oldPassword, String newPassword)
    {
        SysUser user = ShiroUtils.getSysUser();
        if (StringUtils.isNotEmpty(newPassword) && passwordService.matches(user, oldPassword))
        {
            user.setSalt(ShiroUtils.randomSalt());
            user.setPassword(passwordService.encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
            if (userService.resetUserPwd(user) > 0)
            {
                ShiroUtils.setSysUser(userService.selectUserById(user.getUserId()));
                return success();
            }
            return error();
        }
        else
        {
            return error("修改密码失败，旧密码错误");
        }
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit")
    public String edit(ModelMap mmap)
    {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user", userService.selectUserById(user.getUserId()));
        return prefix + "/edit";
    }

    /**
     * 修改头像
     */
    @GetMapping("/avatar")
    public String avatar(ModelMap mmap)
    {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user", userService.selectUserById(user.getUserId()));
        return prefix + "/avatar";
    }

    /**
     * 显示头像
     */
    @GetMapping("/show/avatar/{loginName}")
    public void showAvatar(@PathVariable("loginName") String loginName, HttpServletResponse response)
    {
        try {
            SysUser user;
            if(ShiroUtils.getSysUser().getLoginName().equals(loginName)){
                user = ShiroUtils.getSysUser();
            }else{
                user = userService.selectUserByLoginName(loginName);
            }
            String avatar = MinioClientUtils.getInstance().getAvatarFileName("avatar", FactoryUtils.getFactoryCode(), user.getUserId().toString());
            InputStream inputStream = MinioClientUtils.getInstance().getObject(avatar);
            MinioClientUtils.getInstance().writeBytes(inputStream, response.getOutputStream());
        } catch (Exception e) {
            log.error("获取头像失败！使用默认头像", e);
            try {
                response.sendRedirect(context+"/img/profile.jpg");//默认头像路径，放工程里边
            }catch (Exception e1){
                logger.error("获取默认头像失败",e);
            }
        }
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public AjaxResult update(SysUser user)
    {
        SysUser currentUser = ShiroUtils.getSysUser();
        currentUser.setUserName(user.getUserName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setSex(user.getSex());
        if (userService.updateUserInfo(currentUser) > 0)
        {
            ShiroUtils.setSysUser(userService.selectUserById(currentUser.getUserId()));
            return success();
        }
        return error();
    }

    /**
     * 保存头像
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateAvatar")
    @ResponseBody
    public AjaxResult updateAvatar(@RequestParam("avatarfile") MultipartFile file)
    {
        SysUser currentUser = ShiroUtils.getSysUser();
        try
        {
            if (!file.isEmpty())
            {
                String avatar = "";
                if(Global.isMinioFileStorageMode()){
                    avatar = MinioClientUtils.getInstance().uploadAvatarFile("avatar", file, FactoryUtils.getFactoryCode(), currentUser.getUserId().toString());
                    currentUser.setAvatar("/system/user/profile/show/avatar/"+currentUser.getLoginName());
                }else {
                    avatar = FileUploadUtils.upload(Global.getAvatarPath(), file);
                    currentUser.setAvatar(avatar);
                }
                if (userService.updateUserInfo(currentUser) > 0)
                {
                    ShiroUtils.setSysUser(userService.selectUserById(currentUser.getUserId()));
                    return success();
                }
            }
            return error();
        }
        catch (Exception e)
        {
            log.error("修改头像失败！", e);
            return error(e.getMessage());
        }
    }
}
