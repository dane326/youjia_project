package com.gsh.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gsh.common.annotation.Log;
import com.gsh.common.constant.UserConstants;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.domain.Ztree;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.utils.StringUtils;
import com.gsh.system.domain.*;
import com.gsh.system.service.ISysDeptService;
import com.gsh.system.utils.FactoryUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门信息
 *
 * @author gsh
 */
@Controller
@RequestMapping("/system/factory/dept")
public class SysFactoryDeptController extends BaseController
{
    private String prefix = "system/factory/dept";

    @Autowired
    private ISysDeptService deptService;

    @RequiresPermissions("system:factory:dept:view")
    @GetMapping()
    public String dept(ModelMap mmap)
    {
        SysUser user = FactoryUtils.getSysUser();
        mmap.put("deptId", user.getDeptId());
        return prefix + "/dept";
    }

    @RequiresPermissions("system:factory:dept:list")
    @PostMapping("/list")
    @ResponseBody
    public List<SysDept> list(SysDept dept)
    {
        List<SysDept> deptList = deptService.selectDeptList(dept);
        return deptList;
    }

    /**
     * 新增部门
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap mmap)
    {
        mmap.put("dept", deptService.selectDeptById(parentId));
        // 获取系统信息
        Long fc = FactoryUtils.getFactoryCode();
        mmap.put("factoryCode", fc != null ? fc : "1");
        return prefix + "/add";
    }

    /**
     * 新增保存部门
     */
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:factory:dept:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysDept dept)
    {
        if (UserConstants.DEPT_NAME_NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{deptId}")
    public String edit(@PathVariable("deptId") Long deptId, ModelMap mmap)
    {
        SysDept dept = deptService.selectDeptById(deptId);
        if (StringUtils.isNotNull(dept) && 100L == deptId)
        {
            dept.setParentName("无");
        }
        mmap.put("dept", dept);
        return prefix + "/edit";
    }

    /**
     * 保存
     */
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:factory:dept:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysDept dept)
    {
        if (UserConstants.DEPT_NAME_NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        else if (dept.getParentId().equals(dept.getDeptId()))
        {
            return error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除
     */
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:factory:dept:remove")
    @GetMapping("/remove/{deptId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("deptId") Long deptId)
    {
        if (deptService.selectDeptCount(deptId) > 0)
        {
            return AjaxResult.warn("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return AjaxResult.warn("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }

    /**
     * 校验部门名称
     */
    @PostMapping("/checkDeptNameUnique")
    @ResponseBody
    public String checkDeptNameUnique(SysDept dept)
    {
        return deptService.checkDeptNameUnique(dept);
    }

    /**
     * 选择部门树
     */
    @GetMapping("/selectDeptTree/{deptId}")
    public String selectDeptTree(@PathVariable("deptId") Long deptId, ModelMap mmap)
    {
        mmap.put("dept", deptService.selectDeptById(deptId));
        return prefix + "/tree";
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = deptService.selectDeptTree(new SysDept());
        return ztrees;
    }

    /**
     * 加载角色部门（数据权限）列表树
     */
    @GetMapping("/roleDeptTreeData")
    @ResponseBody
    public List<Ztree> deptTreeData(SysRole role)
    {
        List<Ztree> ztrees = deptService.roleDeptTreeData(role);
        return ztrees;
    }

    @RequiresPermissions("system:factory:dept:view")
    @GetMapping("/orgStructure")
    public String orgStructure(ModelMap mmap) {
        //所有的节点
        Map<String,DeptUserNode> nodemap=new HashMap<String,DeptUserNode>();
        //可查看的全部部门 已经按照父节点排序
        List<SysDept> deptList = deptService.selectDeptList(new SysDept());

        if(CollectionUtils.isNotEmpty(deptList)){  //权限内的根节点
            SysDept rootDept=deptList.get(0);
            DeptUserNode rootNode=new DeptUserNode();
            rootNode.setId(rootDept.getDeptId()+"");
            rootNode.setName(rootDept.getDeptName());
            rootNode.setOpen("true");
            rootNode.setChild(new ArrayList<DeptUserNode>());
            nodemap.put(rootNode.getId(),rootNode);
            //循环构建树图对象
            for(SysDept dept:deptList){
                if (nodemap.containsKey(dept.getParentId()+"")) {
                    List<DeptUserNode> child=nodemap.get(dept.getParentId()+"").getChild();
                    DeptUserNode node=new DeptUserNode(dept.getDeptId()+"",dept.getDeptName(),"true","",new ArrayList<DeptUserNode>());
                    child.add(node);
                    nodemap.put(node.getId(),node);
                }
            }
            JSONArray ja=new JSONArray();
            ja.add(rootNode);
            String orgTreeJsonStr = JSON.toJSONString(ja);
            mmap.put("orgTreeJson",ja);
            System.out.println(orgTreeJsonStr);
        }
        return prefix + "/orgStructure";
    }
}
