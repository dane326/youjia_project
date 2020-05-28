package com.gsh.web.controller.system;

import com.gsh.common.annotation.Log;
import com.gsh.common.constant.UserConstants;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.core.text.Convert;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.exception.BusinessException;
import com.gsh.common.utils.file.FileUtils;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.framework.shiro.service.SysPasswordService;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.*;
import com.gsh.system.service.*;
import com.gsh.system.utils.FactoryUtils;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户信息
 * 
 * @author gsh
 */
@Controller
@RequestMapping("/system/user")
public class SysUserController extends BaseController
{
    private String prefix = "system/user";

    private String prefix_business = "system/factory/user";

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private ISysFactoryLabelService sysFactoryLabelService;

    @RequiresPermissions("system:user:view")
    @GetMapping()
    public String user(ModelMap mmap)
    {
    	SysUser user = ShiroUtils.getSysUser();
        mmap.put("deptId", user.getDeptId());
    	mmap.put("factoryCode", FactoryUtils.getFactoryCode(user));
        return prefix + "/user";
    }


    @RequiresPermissions("system:user:view")
    @GetMapping("/businessuser")
    public String businessuser()
    {
        return prefix_business + "/user";
    }

    @RequiresPermissions("system:user:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUser user,String urltype)
    {
        startPage();
        if ("businessuser".equals(urltype)) {
            Long deptId= FactoryUtils.getSysUser().getDept().getDeptId();
            user.setCreateByDeptid(deptId);//默认查询该部门的数据.
        }
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:user:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysUser user,String urltype)
    {
        if("businessuser".equals(urltype)){
            user.setDeleted("0");//查询未删除的数据
        }
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:user:import")
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        updateSupport=false; //20200115屏蔽导入更新

        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = ShiroUtils.getSysUser().getUserId()+"";
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @RequiresPermissions("system:user:view")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate(HttpServletResponse response, HttpServletRequest request, String urltype)
    {
        if ("businessuser".equals(urltype)) {
            try {
                String fileOrginName = "";
                String realFileName = "";
                response.setCharacterEncoding("utf-8");
                fileOrginName = "factoryUserImportTemplate.xlsx";
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                realFileName = "成员导入Excel模板.xlsx";
                response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
                IOUtils.copyLarge(this.getClass().getResourceAsStream("/excelTemplate/" + fileOrginName), response.getOutputStream());
                return AjaxResult.success("");
            } catch (Exception e) {
                logger.error("下载模板文件失败", e);
                throw new BusinessException("模板无效，请联系网站管理员！");
            }
        } else {
            ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
            return util.importTemplateExcel("用户数据");
        }
    }


    /**
     * 新增用户
     */
    @GetMapping("/add")
    public String add(ModelMap mmap, String urltype)
    {
        String _retu = "";
        if ("businessuser".equals(urltype)) {
            mmap.put("lables", sysFactoryLabelService.selectSysFactoryLabelList(new SysFactoryLabel()));
            _retu = prefix_business + "/add";
        } else {
            mmap.put("roles", roleService.selectRoleAll());
            mmap.put("posts", postService.selectPostAll());
            _retu = prefix + "/add";
        }
        mmap.put("factoryCode", FactoryUtils.getFactoryCode());

        return _retu;
    }
    
    /**
     * 新增用户
     */
    @GetMapping("/{factoryCode}/{deptId}/add")
    public String addByFactoryCode(ModelMap mmap, @PathVariable("factoryCode") Long factoryCode, @PathVariable("deptId") Long deptId, String urltype)
    {
        String _retu = "";
        if ("businessuser".equals(urltype)) {
            mmap.put("lables", sysFactoryLabelService.selectSysFactoryLabelList(new SysFactoryLabel()));
            _retu = prefix_business + "/add";
        } else {
        	SysRole condRole = new SysRole();
        	SysPost condPost = new SysPost();
        	condRole.setFactoryCode(factoryCode);
        	condPost.setFactoryCode(factoryCode);
            mmap.put("roles", roleService.selectRoleAll(condRole));
            mmap.put("posts", postService.selectPostAll(condPost));
            _retu = prefix + "/add";
        }
        mmap.put("factoryCode", factoryCode);
        mmap.put("deptId", deptId);

        return _retu;
    }

    /**
     * 新增保存用户
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysUser user,String urltype,String lableIds)
    {
        if ("businessuser".equals(urltype)) {
            if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName(),user.getFactoryCode())))
            {
                return error("新增成员'" + user.getLoginName() + "'失败，登录账号已存在");
            }
            return toAjax(userService.insertSysFactoryUser(user,lableIds));
        } else {
            if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName())))
            {
                return error("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
            }
            else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
            {
                return error("新增用户'" + user.getLoginName() + "'失败，手机号码已存在");
            }
            else if (!"".equals(user.getEmail()) && UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
            {
                return error("新增用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
            }
            user.setSalt(ShiroUtils.randomSalt());
            user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
            return toAjax(userService.insertUser(user));
        }
    }


    /**
     * 修改用户
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, ModelMap mmap, String urltype)
    {
        String _retu = "";
        if ("businessuser".equals(urltype)) {
            SysUser sysUser = userService.selectUserById(userId);
            mmap.put("sysFactoryUser", sysUser);
            mmap.put("lables", sysFactoryLabelService.selectLabelsByUserId(userId));
            _retu = prefix_business + "/edit";
        } else {
            mmap.put("user", userService.selectUserById(userId));
            mmap.put("roles", roleService.selectRolesByUserId(userId));
            mmap.put("posts", postService.selectPostsByUserId(userId));
            _retu = prefix + "/edit";
        }

        return _retu;
    }

    /**
     * 修改保存用户
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysUser user,String urltype,String lableIds)
    {
        if ("businessuser".equals(urltype)) {
            return toAjax(userService.updateSysFactoryUser(user,lableIds));
        } else {
            userService.checkUserAllowed(user);
            if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
            {
                return error("修改用户'" + user.getLoginName() + "'失败，手机号码已存在");
            }
            else if (!"".equals(user.getEmail())&&UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
            {
                return error("修改用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
            }
            return toAjax(userService.updateUser(user));
        }
    }

    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/resetPwd";
    }

    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwdSave(SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        if (userService.resetUserPwd(user) > 0)
        {
            if (ShiroUtils.getUserId() == user.getUserId())
            {
                ShiroUtils.setSysUser(userService.selectUserById(user.getUserId()));
            }
            return success();
        }
        return error();
    }

    /**
     * 进入授权角色页
     */
    @GetMapping("/authRole/{userId}")
    public String authRole(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        SysUser user = userService.selectUserById(userId);
        // 获取用户所属的角色列表
        List<SysUserRole> userRoles = userService.selectUserRoleByUserId(userId);
        mmap.put("user", user);
        mmap.put("userRoles", userRoles);
        return prefix + "/authRole";
    }

    /**
     * 用户授权角色
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PostMapping("/authRole/insertAuthRole")
    @ResponseBody
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds)
    {
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    @RequiresPermissions("system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(userService.deleteUserByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 校验用户名
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public String checkLoginNameUnique(SysUser user,String urltype)
    {
        if ("businessuser".equals(urltype)) {
            return userService.checkLoginNameUnique(user.getLoginName(), FactoryUtils.getFactoryCode());
        } else {
            return userService.checkLoginNameUnique(user.getLoginName());
        }

    }

    /**
     * 校验手机号码
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(SysUser user)
    {
        return userService.checkPhoneUnique(user);
    }

    /**
     * 校验email邮箱
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(SysUser user)
    {
        return userService.checkEmailUnique(user);
    }

    /**
     * 用户状态修改
     */
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:user:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysUser user,String urltype)
    {
        if ("businessuser".equals(urltype)) {
            //todo 修改成员状态
            return toAjax(userService.changeStatus(user));
        } else {
            userService.checkUserAllowed(user);
            return toAjax(userService.changeStatus(user));
        }
    }

    /**
     * 选择部门树
     */
    @GetMapping("/selectUserTree/{parentDeptId}")
    public String selectUserTree(@PathVariable("parentDeptId") Long parentDeptId, ModelMap mmap)
    {
        mmap.put("parentDeptId", parentDeptId);
        return prefix + "/tree";
    }

    /**
     * 选择部门树带参数
     */
    @GetMapping("/selectUserTree/{parentDeptId}/{paramId}")
    public String selectUserTree(@PathVariable("parentDeptId") Long parentDeptId, @PathVariable("paramId")String paramId, ModelMap mmap)
    {
        mmap.put("parentDeptId", parentDeptId);
        mmap.put("paramId", paramId);
        return prefix + "/tree";
    }

    /**
     * 加载部门列表树
     */
    @PostMapping("/treeData")
    @ResponseBody
    public Object treeData(Boolean root, Long id, String searchKey)
    {
        return userService.selectDeptUserTree(root, id, searchKey);
    }

    /**
     * 批量更新字段
     * @param userIds
     * @param mmap
     * @return
     */
    @GetMapping("/editDept")
    public String updateSysFactoryUserDeptByIds(String userIds,ModelMap mmap){
        mmap.addAttribute("userIds",userIds);
        return prefix_business + "/editDept";
    }

    /**
     * 子管理员
     * @return
     */
    @GetMapping("/subAdmin")
    @RequiresPermissions("system:factory:role:update")
    public String subAdmin(String userId,ModelMap mmap){
        mmap.addAttribute("userId",userId);
        return prefix_business + "/subAdmin";
    }

    /**
     * 添加子管理员
     * @param roleIds
     * @return
     */
    @PostMapping("/subAdmin")
    @ResponseBody
    public AjaxResult subAdmin(Long userId, String roleIds,String roleName){

        SysUser sysFactoryUser = userService.selectUserById(userId);
        SysUser user = null;
        //更新角色信息
        user = userService.selectUserByLoginName(sysFactoryUser.getLoginName());
        user.setRoleIds( Convert.toLongArray(roleIds));
        userService.updateUser(user);
        return toAjax(userService.updateSysFactoryUser(sysFactoryUser,""));
    }

    /**
     * 取消成员的管理员角色
     * @param userIds
     * @return
     */
    @PostMapping("/cancelAdmin")
    @ResponseBody
    @RequiresPermissions("system:factory:role:delete")
    public AjaxResult cancelAdmin(String userIds){
        //1.更新sys_factory_user 的管理员账号为空
        SysUser sysFactoryUser = userService.selectUserById(Long.parseLong(userIds));
       /* try {
            userService.deleteUserById(sysFactoryUser.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //2.更新sys_user表中的记录
        // sysFactoryUser.setManagerAccount("");
        //sysFactoryUser.setRoleName("");
        // sysFactoryUser.setUserType("student");
        return toAjax(userService.updateSysUsersByIds(userIds,sysFactoryUser));
    }
    /**
     * 子管理员
     * @return
     */
    @GetMapping("/reSetPassWord")
    public String reSetPassWord(Long userId,ModelMap mmap){
        mmap.addAttribute("sysFactoryUser",userService.selectUserById(userId));
        return prefix_business + "/reSetPassWord";
    }

    /**
     * 批量更新字段
     * @param userIds
     * @param sysFactoryUser
     * @return
     */
    @PostMapping("/editAll")
    @ResponseBody
    public AjaxResult updateSysFactoryUserByIds(String userIds,  SysUser sysFactoryUser){
        return toAjax(userService.updateSysFactoryUsersByIds(userIds,sysFactoryUser));
    }
}