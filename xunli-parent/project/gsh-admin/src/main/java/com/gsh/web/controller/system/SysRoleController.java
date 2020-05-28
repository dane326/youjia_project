package com.gsh.web.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gsh.system.utils.FactoryUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gsh.common.annotation.Log;
import com.gsh.common.constant.UserConstants;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.domain.Ztree;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.SysMenu;
import com.gsh.system.domain.SysRole;
import com.gsh.system.domain.SysUser;
import com.gsh.system.domain.SysUserRole;
import com.gsh.system.service.ISysMenuService;
import com.gsh.system.service.ISysRoleService;
import com.gsh.system.service.ISysUserService;

/**
 * 角色信息
 * 
 * @author gsh
 */
@Controller
@RequestMapping("/system/role")
public class SysRoleController extends BaseController
{
    private String prefix = "system/role";

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysMenuService menuService;

    @RequiresPermissions("system:role:view")
    @GetMapping()
    public String role(ModelMap mmap)
    {
        mmap.put("factoryCode", FactoryUtils.getFactoryCode());
        if(FactoryUtils.getFactoryTreeMode()){
        	return prefix + "/roleTree";
        }
        return prefix + "/role";
    }

    @RequiresPermissions("system:role:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysRole role)
    {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }
    
    @RequiresPermissions("system:role:list")
    @PostMapping("/list/all")
    @ResponseBody
    public TableDataInfo listAll(SysRole role)
    {
        startPage();
        List<SysRole> list = roleService.selectRoleAll(role);
        return getDataTable(list);
    }

    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:role:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysRole role)
    {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        return util.exportExcel(list, "角色数据");
    }

    /**
     * 新增角色
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        // 获取系统信息
    	mmap.put("factoryCode", FactoryUtils.getFactoryCode());
        return prefix + "/add";
    }
    
    /**
     * 新增角色
     */
    @GetMapping("/{factoryCode}/add")
    public String addByFactoryCode(@PathVariable("factoryCode") Long factoryCode, ModelMap mmap)
    {
        mmap.put("factoryCode", factoryCode);
        return prefix + "/add";
    }

    /**
     * 新增保存角色
     */
    @RequiresPermissions("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysRole role)
    {
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role)))
        {
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        else if (UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role)))
        {
            return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        ShiroUtils.clearCachedAuthorizationInfo();
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改角色
     */
    @GetMapping("/edit/{roleId}")
    public String edit(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/edit";
    }

    /**
     * 修改保存角色
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysRole role)
    {
        roleService.checkRoleAllowed(role);
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role)))
        {
            return error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        else if (UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role)))
        {
            return error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        ShiroUtils.clearCachedAuthorizationInfo();
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 角色分配数据权限
     */
    @GetMapping("/authDataScope/{roleId}")
    public String authDataScope(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/dataScope";
    }

    /**
     * 角色分配数据权限(矩阵模式)
     */
    @GetMapping("/authDataScopeDane/{roleId}")
    public String authDataScopeDane(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        //List<SysMenu> allmenulist = menuService.selectMenuList(new SysMenu(),Long.parseLong("1"));//一级菜单，模块
        mmap.put("role", roleService.selectRoleById(roleId));
        //获取系统功能列表
        List<SysMenu> fristLevelList = menuService.listbyparentid(Long.parseLong("0"),"'M'");//一级菜单，模块
        Map tempMap = new HashMap();//菜单数据
        Map functionMap = new HashMap();//功能点数据
        for(SysMenu menu : fristLevelList ){
            List menulist = new ArrayList();
            List<SysMenu> secondLevelList = menuService.listbyparentid(menu.getMenuId(),"'M','C'");//二级菜单
            for(SysMenu secondMenu : secondLevelList){
                List<SysMenu> thirdLevelList = menuService.listbyparentid(secondMenu.getMenuId(),"'M','C'");//三级菜单
                if(thirdLevelList.size() > 0){//存在三级子菜单，则二三级菜单拼接成新菜单
                    for(SysMenu thirdMenu : thirdLevelList){
                        Map subMap = new HashMap();
                        subMap.put("menuid",thirdMenu.getMenuId());
                        subMap.put("menuname",secondMenu.getMenuName()+"-->"+thirdMenu.getMenuName());
                        menulist.add(subMap);
                    }
                 } else {
                    Map subMap = new HashMap();
                    subMap.put("menuid",secondMenu.getMenuId());
                    subMap.put("menuname",secondMenu.getMenuName()+"-->"+secondMenu.getMenuName());
                    menulist.add(subMap);
                }
            }
            tempMap.put(menu.getMenuId(),menulist);
        }

        /*for (Object key : tempMap.keySet()) {
            List menulist = (List)tempMap.get(key);
            for(int i=0;menulist != null && i<menulist.size();i++){
                Map temp = (Map) menulist.get(i);
                for (Object key1 : temp.keySet()) {
                    //递归获取功能清单
                    List functionlist = menuService.listAllSubMenu(allmenulist,Long.parseLong(key1.toString()));
                    functionMap.put(key1,functionlist);
                }
            }
        }*/

       // List functionlist = menuService.listAllSubMenu(allmenulist,Long.parseLong("2096"));

        mmap.put("functionMap",functionMap);
        mmap.put("tempMap",tempMap);
        mmap.put("fristLevelList",fristLevelList);
        System.out.println("functionMap == "+functionMap);
        System.out.println("tempMap == "+tempMap);
        return prefix + "/authDataScopeDane";
    }

    /**
     * 保存角色分配数据权限
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/authDataScope")
    @ResponseBody
    public AjaxResult authDataScopeSave(SysRole role)
    {
        roleService.checkRoleAllowed(role);
        if (roleService.authDataScope(role) > 0)
        {
            ShiroUtils.setSysUser(userService.selectUserById(ShiroUtils.getSysUser().getUserId()));
            return success();
        }
        return error();
    }

    @RequiresPermissions("system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(roleService.deleteRoleByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 校验角色名称
     */
    @PostMapping("/checkRoleNameUnique")
    @ResponseBody
    public String checkRoleNameUnique(SysRole role)
    {
        return roleService.checkRoleNameUnique(role);
    }

    /**
     * 校验角色权限
     */
    @PostMapping("/checkRoleKeyUnique")
    @ResponseBody
    public String checkRoleKeyUnique(SysRole role)
    {
        return roleService.checkRoleKeyUnique(role);
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree")
    public String selectMenuTree()
    {
        return prefix + "/tree";
    }

    /**
     * 角色状态修改
     */
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:role:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysRole role)
    {
        roleService.checkRoleAllowed(role);
        return toAjax(roleService.changeStatus(role));
    }

    /**
     * 分配用户
     */
    @RequiresPermissions("system:role:edit")
    @GetMapping("/authUser/{roleId}")
    public String authUser(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/authUser";
    }

    /**
     * 查询已分配用户角色列表
     */
    @RequiresPermissions("system:role:list")
    @PostMapping("/authUser/allocatedList")
    @ResponseBody
    public TableDataInfo allocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权
     */
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/cancel")
    @ResponseBody
    public AjaxResult cancelAuthUser(SysUserRole userRole)
    {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权
     */
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/cancelAll")
    @ResponseBody
    public AjaxResult cancelAuthUserAll(Long roleId, String userIds)
    {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 选择用户
     */
    @GetMapping("/authUser/selectUser/{roleId}")
    public String selectUser(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/selectUser";
    }

    /**
     * 查询未分配用户角色列表
     */
    @RequiresPermissions("system:role:list")
    @PostMapping("/authUser/unallocatedList")
    @ResponseBody
    public TableDataInfo unallocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 批量选择用户授权
     */
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/selectAll")
    @ResponseBody
    public AjaxResult selectAuthUserAll(Long roleId, String userIds)
    {
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }

    /**
     * 选择角色树
     */
    @GetMapping("/selectRoleTree/{columnId}")
    public String selectDeptTree(@PathVariable("columnId") Long columnId, ModelMap mmap)
    {
        mmap.put("columnId", columnId);
        return prefix + "/tree";
    }


    /**
     * 加载角色列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = roleService.selectDictTree(new SysRole());
        return ztrees;
    }


    //dane 测试角色模块
    /**
     * 新增角色
     */
    @GetMapping("/distribution")
    public String distribution(ModelMap mmap)
    {
        // 获取系统信息
        mmap.put("factoryCode", FactoryUtils.getFactoryCode());
        return prefix + "/distribution";
    }
}