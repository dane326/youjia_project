//package com.gsh.web.controller.portalet;
package com.gsh.system.portalet.controller;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gsh.common.annotation.Log;
import com.gsh.common.config.Global;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.domain.Ztree;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.utils.file.FileUploadUtils;
import com.gsh.common.utils.file.MinioClientUtils;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.system.portalet.domain.PortaletRes;
import com.gsh.system.portalet.service.IPortaletResService;
import com.gsh.system.portalet.utils.PortaletUtils;

/**
 * 可配置资源信息Controller
 * 
 * @author gsh
 * @date 2020-03-15
 */
@Controller
@RequestMapping("/system/portalet/res")
public class PortaletResController extends BaseController {
	private String prefix = "system/portalet/res";

	@Autowired
	private IPortaletResService portaletResService;

	@RequiresPermissions("system:portalet:res:view")
	@GetMapping()
	public String res() {
		return prefix + "/res";
	}
	
	/**
	 * 查询可配置资源信息列表
	 */
	@RequiresPermissions("system:portalet:res:list")
	@PostMapping("/list")
	@ResponseBody
	public List<PortaletRes> list(PortaletRes portaletRes) {
		return portaletResService.selectPortaletResList(portaletRes);
	}

	/**
	 * 导出可配置资源信息列表
	 */
	@RequiresPermissions("system:portalet:res:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(PortaletRes portaletRes) {
		List<PortaletRes> list = portaletResService.selectPortaletResList(portaletRes);
		ExcelUtil<PortaletRes> util = new ExcelUtil<PortaletRes>(PortaletRes.class);
		return util.exportExcel(list, "res");
	}

	/**
	 * 新增可配置资源信息
	 */
	@RequiresPermissions("system:portalet:res:add")
	@GetMapping("/add/{parentId}")
	public String add(@PathVariable("parentId") Long parentId, ModelMap mmap) {
		PortaletRes res = null;
		if (0L != parentId) {
			res = portaletResService.selectPortaletResById(parentId);
			if("sys".equals(res.getResType())){
				res.setResType("module");
			} else if("module".equals(res.getResType())){
				res.setResType("res");
			}
		} else {
			res = new PortaletRes();
			res.setId(0L);
			res.setResType("sys");
		}
		mmap.put("res", res);
		return prefix + "/add";
	}

	/**
	 * 新增保存可配置资源信息
	 */
	@RequiresPermissions("system:portalet:res:add")
	@Log(title = "可配置资源信息", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PortaletRes portaletRes) {
		return toAjax(portaletResService.insertPortaletRes(portaletRes));
	}

	/**
	 * 修改可配置资源信息
	 */
	@RequiresPermissions("system:portalet:res:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, @RequestParam(value = "showButtonFlag", required = false) Boolean showButtonFlag, ModelMap mmap) {
		PortaletRes portaletRes = portaletResService.selectPortaletResById(id);
		mmap.put("portaletRes", portaletRes);
		mmap.put("showButtonFlag", showButtonFlag);
		return prefix + "/edit";
	}

	/**
	 * 修改保存可配置资源信息
	 */
	@RequiresPermissions("system:portalet:res:edit")
	@Log(title = "可配置资源信息", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PortaletRes portaletRes) {
		return toAjax(portaletResService.updatePortaletRes(portaletRes));
	}

	/**
	 * 删除可配置资源信息
	 */
	@RequiresPermissions("system:portalet:res:remove")
	@Log(title = "可配置资源信息", businessType = BusinessType.DELETE)
	@GetMapping("/remove/{id}")
	@ResponseBody
	public AjaxResult remove(@PathVariable("id") Long id) {
		return toAjax(portaletResService.logicDeletePortaletResById(id));
	}
	
	@RequiresPermissions("system:portalet:res:edit")
	@GetMapping("/{id}/{width}/{height}/avatar")
    public String avatar(@PathVariable("id") Long id, @PathVariable("width") Integer width, @PathVariable("height") Integer height, ModelMap mmap) {
		PortaletRes portaletRes = portaletResService.selectPortaletResById(id);
        mmap.put("id", id);
		mmap.put("avatar", portaletRes.getImgUrl());
		mmap.put("width", width);
		mmap.put("height", height);
        return prefix + "/avatar";
    }
	
	@RequiresPermissions("system:portalet:res:edit")
	@GetMapping("/{id}/avatar")
    public String avatar(@PathVariable("id") Long id, ModelMap mmap) {
		PortaletRes portaletRes = portaletResService.selectPortaletResById(id);
        mmap.put("id", id);
		mmap.put("avatar", portaletRes.getImgUrl());
        return prefix + "/avatar";
    }
	
	/**
     * 显示
     */
	@RequiresPermissions("system:portalet:res:view")
    @GetMapping("show/avatar/{id}")
    public void showAvatar(@PathVariable("id") Long id, HttpServletResponse response)
    {
        try {
            String avatar = PortaletUtils.getInstance().getImageFileName("portalet", id);
            InputStream inputStream = MinioClientUtils.getInstance().getObject(avatar);
            MinioClientUtils.getInstance().writeBytes(inputStream, response.getOutputStream());
        } catch (Exception e) {
            logger.error("获取图片失败", e);
        }
    }

    /**
     * 保存图片
     */
    @RequiresPermissions("system:portalet:res:edit")
    @Log(title = "可配置资源信息", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/updateAvatar")
    @ResponseBody
    public AjaxResult updateAvatar(@PathVariable("id") Long id, @RequestParam("avatarfile") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String avatar = "";
                if(Global.isMinioFileStorageMode()) {
                    avatar = PortaletUtils.getInstance().uploadImage("portalet", id, file);
                    avatar = String.format("/system/portalet/res/show/avatar/%s", String.valueOf(id));
                }else{
                    avatar = FileUploadUtils.upload(Global.getLogoPath(), file);
                }
                PortaletRes portaletRes = new PortaletRes();
                portaletRes.setId(id);
                portaletRes.setImgUrl(avatar);
                if (portaletResService.updatePortaletRes(portaletRes) > 0) {
                    return success(avatar);
                }
            }
            return error();
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }
    
    /**
     * 加载资源列表树
     */
    @RequiresPermissions("system:portalet:res:view")
    @GetMapping("/treeData")
    @ResponseBody
	public List<Ztree> treeData(PortaletRes portaletRes, @RequestParam(value = "parentNoCheckFlag", required = false) Boolean parentNoCheckFlag) {
		List<Ztree> ztrees = portaletResService.selectResTree(portaletRes, parentNoCheckFlag);
		return ztrees;
	}
}
