//package com.gsh.system.web.controller.system:portalet;
package com.gsh.system.portalet.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gsh.common.annotation.Log;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.enums.BusinessType;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.SysUser;
import com.gsh.system.portalet.domain.PortaletPage;
import com.gsh.system.portalet.domain.PortaletRes;
import com.gsh.system.portalet.domain.PortaletUser;
import com.gsh.system.portalet.service.IPortaletPageService;
import com.gsh.system.portalet.service.IPortaletResService;
import com.gsh.system.portalet.service.IPortaletUserService;

/**
 * 用户配置的资源信息Controller
 * 
 * @author gsh
 * @date 2020-03-17
 */
@Controller
@RequestMapping("/system/portalet/user")
public class PortaletUserController extends BaseController {
	private String prefix = "system/portalet/user";

	@Autowired
	private IPortaletUserService portaletUserService;
	
	@Autowired
	private IPortaletResService portaletResService;
	
	@Autowired
	private IPortaletPageService portaletPageService;

	@GetMapping("/main/view")
    public String mainView(ModelMap mmap) {
		SysUser user = ShiroUtils.getSysUser();
		String loginName = user.getLoginName();
		PortaletUser portaletUser = new PortaletUser();
		portaletUser.setUserCode(loginName);
		portaletUser.setDeleted("0");
		List<PortaletUser> portaletUsers = portaletUserService.selectPortaletUserList(portaletUser);
		if(CollectionUtils.isNotEmpty(portaletUsers)){
			mmap.put("portaletUsers", portaletUserService.selectPortaletUserList(portaletUser));
		} else {
			PortaletPage portaletPage = new PortaletPage();
			portaletPage.setResType("res");
			portaletPage.setDeleted("0");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userRoleKeys", user.getRoles().stream().map(x -> "role_" + x.getRoleId()).collect(Collectors.toList()));
			portaletPage.setParams(params);
			mmap.put("portaletUsers", portaletPageService.selectPortaletPageList(portaletPage));
		}
		
		mmap.put("userCode", loginName);
		mmap.put("isAdmin", ShiroUtils.getSysUser().isAdmin());
        return prefix + "/mainView";
    }
	
	@GetMapping("/main/edit")
    public String mainEdit(ModelMap mmap) {
		String loginName = ShiroUtils.getSysUser().getLoginName();
		PortaletUser portaletUser = new PortaletUser();
		portaletUser.setUserCode(loginName);
		portaletUser.setDeleted("0");
		mmap.put("portaletUsers", portaletUserService.selectPortaletUserList(portaletUser));
		mmap.put("userCode", loginName);
        return prefix + "/mainEdit";
    }
	
	@GetMapping("/res/{userCode}")
	public String resList(@PathVariable("userCode") String userCode, ModelMap mmap) {
		mmap.put("userCode", userCode);
		return prefix + "/selectRes";
	}
	
	/**
	 * 查询可配置资源信息列表
	 */
	@PostMapping("/selectResList")
	@ResponseBody
	public TableDataInfo selectResList(PortaletRes portaletRes) {
		List<PortaletRes> list = portaletResService.selectPortaletResList(portaletRes);
		return getDataTable(list);
	}
	
	/**
	 * 修改保存用户配置的资源信息
	 */
	@Log(title = "用户配置的资源信息", businessType = BusinessType.UPDATE)
	@PostMapping("/edits")
	@ResponseBody
	public AjaxResult editsSave(@RequestBody List<PortaletUser> portaletUsers) {
		return toAjax(portaletUserService.updatePortaletUsers(portaletUsers));
	}

	/**
	 * 删除用户配置的资源信息
	 */
	@Log(title = "用户配置的资源信息", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(portaletUserService.deletePortaletUserByIds(ids));
	}
	
	/**
	 * 删除自定义portal页面
	 */
	@Log(title = "自定义portal页面", businessType = BusinessType.DELETE)
	@GetMapping("/remove/{id}")
	@ResponseBody
	public AjaxResult remove(@PathVariable("id") Long id) {
		return toAjax(portaletUserService.deletePortaletUserById(id));
	}
	
	@Log(title = "自定义portal页面", businessType = BusinessType.INSERT)
	@PostMapping("/{userCode}/addRes")
	@ResponseBody
	public AjaxResult addRes(@PathVariable("userCode") String userCode, String ids) {
		return toAjax(portaletUserService.insertResByIds(userCode, ids));
	}
}
