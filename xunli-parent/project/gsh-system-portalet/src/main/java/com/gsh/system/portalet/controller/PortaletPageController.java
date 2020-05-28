//package com.gsh.system.web.controller.system:portalet;
package com.gsh.system.portalet.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.portalet.domain.PortaletPage;
import com.gsh.system.portalet.domain.PortaletRes;
import com.gsh.system.portalet.service.IPortaletPageService;
import com.gsh.system.portalet.service.IPortaletResService;

/**
 * 自定义portal页面Controller
 * 
 * @author gsh
 * @date 2020-03-17
 */
@Controller
@RequestMapping("/system/portalet/page")
public class PortaletPageController extends BaseController {
	private String prefix = "system/portalet/page";

	@Autowired
	private IPortaletPageService portaletPageService;
	
	@Autowired
	private IPortaletResService portaletResService;
	
	@RequiresPermissions("system:portalet:page:view")
	@GetMapping("/main/view/{modulePageId}")
    public String mainView(@PathVariable("modulePageId") Long modulePageId, ModelMap mmap) {
		PortaletPage portaletPage = new PortaletPage();
		portaletPage.setResType("res");
		portaletPage.setDeleted("0");
		portaletPage.setParentId(modulePageId);
		mmap.put("portaletPages", portaletPageService.selectPortaletPageList(portaletPage));
		mmap.put("modulePageId", modulePageId);
		mmap.put("isAdmin", ShiroUtils.getSysUser().isAdmin());
        return prefix + "/mainView";
    }
	
	@RequiresPermissions("system:portalet:page:view")
	@GetMapping("/main/edit/{modulePageId}")
    public String mainEdit(@PathVariable("modulePageId") Long modulePageId, ModelMap mmap) {
		PortaletPage portaletPage = new PortaletPage();
		portaletPage.setResType("res");
		portaletPage.setDeleted("0");
		portaletPage.setParentId(modulePageId);
		mmap.put("portaletPages", portaletPageService.selectPortaletPageList(portaletPage));
		mmap.put("modulePageId", modulePageId);
        return prefix + "/mainEdit";
    }

	@RequiresPermissions("system:portalet:page:view")
	@GetMapping()
	public String page() {
		return prefix + "/page";
	}
	
	@RequiresPermissions("system:portalet:page:view")
	@GetMapping("/res/{modulePageId}")
	public String resList(@PathVariable("modulePageId") Long modulePageId, ModelMap mmap) {
		mmap.put("modulePageId", modulePageId);
		return prefix + "/selectRes";
	}
	
	/**
	 * 查询可配置资源信息列表
	 */
	@RequiresPermissions("system:portalet:page:view")
	@PostMapping("/selectResList")
	@ResponseBody
	public TableDataInfo selectResList(PortaletRes portaletRes) {
		List<PortaletRes> list = portaletResService.selectPortaletResList(portaletRes);
		return getDataTable(list);
	}

	/**
	 * 查询自定义portal页面列表
	 */
	@RequiresPermissions("system:portalet:page:list")
	@PostMapping("/list")
	@ResponseBody
	public List<PortaletPage> list(PortaletPage portaletPage) {
		return portaletPageService.selectPortaletPageList(portaletPage);
	}

	/**
	 * 导出自定义portal页面列表
	 */
	@RequiresPermissions("system:portalet:page:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(PortaletPage portaletPage) {
		List<PortaletPage> list = portaletPageService.selectPortaletPageList(portaletPage);
		ExcelUtil<PortaletPage> util = new ExcelUtil<PortaletPage>(PortaletPage.class);
		return util.exportExcel(list, "page");
	}

	/**
	 * 新增自定义portal页面
	 */
	@RequiresPermissions("system:portalet:page:add")
	@GetMapping("/add/{parentId}")
	public String add(@PathVariable("parentId") Long parentId, ModelMap mmap) {
		PortaletPage page = null;
		if (0L != parentId) {
			page = portaletPageService.selectPortaletPageById(parentId);
			if ("sys".equals(page.getResType())) {
				page.setResType("module");
			} else if ("module".equals(page.getResType())) {
				page.setResType("res");
			}
		} else {
			page = new PortaletPage();
			page.setId(0L);
			page.setResType("sys");
		}
		mmap.put("portaletPage", page);
		return prefix + "/add";
	}

	/**
	 * 新增保存自定义portal页面
	 */
	@RequiresPermissions("system:portalet:page:add")
	@Log(title = "自定义portal页面", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PortaletPage portaletPage) {
		return toAjax(portaletPageService.insertPortaletPage(portaletPage));
	}

	/**
	 * 修改自定义portal页面
	 */
	@RequiresPermissions("system:portalet:page:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		PortaletPage portaletPage = portaletPageService.selectPortaletPageById(id);
		mmap.put("portaletPage", portaletPage);
		return prefix + "/edit";
	}

	/**
	 * 修改保存自定义portal页面
	 */
	@RequiresPermissions("system:portalet:page:edit")
	@Log(title = "自定义portal页面", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PortaletPage portaletPage) {
		return toAjax(portaletPageService.updatePortaletPage(portaletPage));
	}
	
	/**
	 * 修改保存自定义portal页面
	 */
	@RequiresPermissions("system:portalet:page:edit")
	@Log(title = "自定义portal页面", businessType = BusinessType.UPDATE)
	@PostMapping("/edits")
	@ResponseBody
	public AjaxResult editsSave(@RequestBody List<PortaletPage> portaletPages) {
		return toAjax(portaletPageService.updatePortaletPages(portaletPages));
	}

	/**
	 * 删除自定义portal页面
	 */
	@RequiresPermissions("system:portalet:page:remove")
	@Log(title = "自定义portal页面", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(portaletPageService.deletePortaletPageByIds(ids));
	}
	
	/**
	 * 删除自定义portal页面
	 */
	@RequiresPermissions("system:portalet:page:remove")
	@Log(title = "自定义portal页面", businessType = BusinessType.DELETE)
	@GetMapping("/remove/{id}")
	@ResponseBody
	public AjaxResult remove(@PathVariable("id") Long id) {
		return toAjax(portaletPageService.deletePortaletPageById(id));
	}
	
	@RequiresPermissions("system:portalet:page:add")
	@Log(title = "自定义portal页面", businessType = BusinessType.INSERT)
	@PostMapping("/{modulePageId}/addRes")
	@ResponseBody
	public AjaxResult addRes(@PathVariable("modulePageId") Long modulePageId, String ids) {
		return toAjax(portaletPageService.insertResByIds(modulePageId, ids));
	}
	
}
