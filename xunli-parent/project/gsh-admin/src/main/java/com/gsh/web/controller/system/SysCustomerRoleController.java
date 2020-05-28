package com.gsh.web.controller.system;

import com.gsh.common.annotation.Log;
import com.gsh.common.constant.UserConstants;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.domain.Ztree;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.enums.BusinessType;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.SysMenu;
import com.gsh.system.domain.SysRole;
import com.gsh.system.service.ISysMenuService;
import com.gsh.system.service.ISysRoleService;
import com.gsh.system.utils.FactoryUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by Administrator on 2020/2/27 0027.
 */
@Controller
@RequestMapping("/system/customerrole")
public class SysCustomerRoleController extends BaseController {
    private String prefix = "system/customerrole";

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    //@RequiresPermissions("system:customerrole:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysRole role)
    {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @GetMapping("/customerdistribution")
    public String distributionnew(ModelMap mmap)
    {
        // 获取系统信息
        Long fc = FactoryUtils.getFactoryCode();
        mmap.put("factoryCode", fc != null ? fc : "1");
        return prefix + "/distributionnew";
    }


    /**
     * 角色状态修改
     */
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysRole role)
    {
        roleService.checkRoleAllowed(role);
        return toAjax(roleService.changeStatus(role));
    }

    /**
     * 角色分配数据权限(矩阵模式)
     */
    @GetMapping("/authDataScopeDane/{roleId}")
    public String authDataScopeDane(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        SysRole role = roleService.selectRoleById(roleId);
        List<SysMenu> allmenulist = menuService.selectMenuList(new SysMenu(),Long.parseLong("1"));//一级菜单，模块

        //获取已经选中的菜单
        Long userId = ShiroUtils.getUserId();
        List<Ztree> ztrees = menuService.roleMenuTreeData(role, userId);

        //一级目录
        List<SysMenu> fristLevelList = getFristSysMenus(ztrees);

        //获取系统功能列表
        Map tempMap = getSysMenuMap(ztrees, fristLevelList);
        Map functionMap = new HashMap();//功能点数据
        getAllFunction(allmenulist, tempMap, functionMap);

        String isChecked = getIsChecked(ztrees);
        mmap.put("isChecked",isChecked);
        mmap.put("functionMap",functionMap);
        mmap.put("tempMap",tempMap);
        mmap.put("fristLevelList",fristLevelList);
        mmap.put("role",role);

        return prefix + "/authDataScopeDane";
    }

    private Map getSysMenuMap(List<Ztree> ztrees, List<SysMenu> fristLevelList) {
        Map tempMap = new HashMap();//菜单数据
        for(SysMenu menu : fristLevelList ){
            //获取第二级菜单列表
            List<SysMenu> secondLevelList = getSecondSysMenus(ztrees, menu);
            //菜单列表
            List menulist = getMenuList(secondLevelList);
            if(menulist.size() > 0){
                tempMap.put(menu.getMenuId(),menulist);
            }
        }
        return tempMap;
    }

    private String getIsChecked(List<Ztree> ztrees) {
        String isChecked = "";
        for(Ztree tree:ztrees){
            boolean checked = tree.isChecked();
            if(checked){
                if("".equals(isChecked)){
                    isChecked = tree.getId().toString();
                } else {
                    isChecked += "," + tree.getId();
                }
            }
        }
        return isChecked;
    }

    private void getAllFunction(List<SysMenu> allmenulist, Map tempMap, Map functionMap) {
        for (Object key : tempMap.keySet()) {
            List menulist = (List)tempMap.get(key);
            for(int i=0;menulist != null && i<menulist.size();i++){
                Map temp = (Map) menulist.get(i);
                for (Object key1 : temp.keySet()) {
                    //递归获取功能清单
                    if("menuid".equals(key1.toString())){
                        String str = temp.get(key1).toString();
                        List functionlist = listAllSubMenu(allmenulist,Long.parseLong(str));
                        if(functionlist.size() > 0) {
                            functionMap.put(str, functionlist);
                        }
                    }
                }
            }
        }
    }

    private List getMenuList(List<SysMenu> secondLevelList) {
        List menulist = new ArrayList();
        for(SysMenu secondMenu : secondLevelList){
            Map subMap = new HashMap();
            subMap.put("menuid",secondMenu.getMenuId());
            subMap.put("menuname",secondMenu.getMenuName());
            menulist.add(subMap);
        }
        return menulist;
    }

    private List<SysMenu> getSecondSysMenus(List<Ztree> ztrees, SysMenu menu) {
        List<SysMenu> secondLevelList = new ArrayList<>();
        for(Ztree _tree2 :ztrees){
            if((menu.getMenuId().toString().trim()).equals(_tree2.getpId().toString().trim())){
                SysMenu _menu2 = new SysMenu();
                _menu2.setMenuId(_tree2.getId());
                _menu2.setMenuName(_tree2.getTitle());
                secondLevelList.add(_menu2);
            }
        }
        return secondLevelList;
    }

    private List<SysMenu> getFristSysMenus(List<Ztree> ztrees) {
        List<SysMenu> fristLevelList = new ArrayList<>();
        for(Ztree tree :ztrees){
            if(tree.getpId() == Long.parseLong("0")){
                SysMenu _menu = new SysMenu();
                _menu.setMenuId(tree.getId());
                _menu.setMenuName(tree.getTitle());
                fristLevelList.add(_menu);
            }
        }
        return fristLevelList;
    }

    public List listAllSubMenu(List<SysMenu> menuList, Long parentId) {
        Iterator var3 = menuList.iterator();
        List childMenu = new ArrayList();

        while(var3.hasNext()) {
            SysMenu mu = (SysMenu)var3.next();
            if(mu.getParentId().equals(parentId)) {
                this.listAllSubMenu(menuList, mu.getMenuId());
                Map map = new HashMap();
                map.put("menuid", mu.getMenuId());
                map.put("menuname", mu.getMenuName());
                childMenu.add(map);
            }
        }

        return childMenu;
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
     * 新增角色
     */
    @GetMapping("/addRole")
    public String addRole(ModelMap mmap)
    {
        // 获取系统信息
        Long fc = FactoryUtils.getFactoryCode();
        mmap.put("factoryCode", fc != null ? fc : "1");
        return prefix + "/addRole";
    }

    /**
     * 新增保存角色
     */
    @RequiresPermissions("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/addRole")
    @ResponseBody
    public AjaxResult addSave(@Validated SysRole role)
    {
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role)))
        {
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        ShiroUtils.clearCachedAuthorizationInfo();
        AjaxResult result = toAjax(roleService.insertRole(role));
        if(result != null){
            System.out.println("result == " + result);
        }
        return result;

    }
}
