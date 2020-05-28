package com.gsh.system.controller;

import com.gsh.common.annotation.Log;
import com.gsh.common.config.Global;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.exception.file.InvalidExtensionException;
import com.gsh.common.utils.file.MimeTypeUtils;
import com.gsh.common.utils.file.MinioClientUtils;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.SysAccessories;
import com.gsh.system.service.ISysAccessoriesService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 附件Controller
 * 
 * @author gsh
 * @date 2019-09-24
 */
@Controller
@RequestMapping("/system/accessories")
public class SysAccessoriesController extends BaseController {
    private String prefix = "system/accessories";

    @Autowired
    private ISysAccessoriesService sysAccessoriesService;

    @RequiresPermissions("system:accessories:view")
    @GetMapping()
    public String accessories() {
        return prefix + "/accessories";
    }

    /**
     * 查询附件列表
     */
    @RequiresPermissions("system:accessories:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysAccessories sysAccessories) {
        startPage();
        Long deptId = ShiroUtils.getSysUser().getDept().getDeptId();
        sysAccessories.setCreateByDeptid(deptId);//默认查询该部门的数据.
        sysAccessories.setDeleted("0");//查询未删除的数据
        List<SysAccessories> list = sysAccessoriesService.selectSysAccessoriesList(sysAccessories);
        return getDataTable(list);
    }

    /**
     * 导出附件列表
     */
    @RequiresPermissions("system:accessories:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysAccessories sysAccessories) {
        Long deptId = ShiroUtils.getSysUser().getDept().getDeptId();
        sysAccessories.setCreateByDeptid(deptId);//默认查询该部门的数据.
        sysAccessories.setDeleted("0");//查询未删除的数据
        List<SysAccessories> list = sysAccessoriesService.selectSysAccessoriesList(sysAccessories);
        ExcelUtil<SysAccessories> util = new ExcelUtil<SysAccessories>(SysAccessories.class);
        return util.exportExcel(list, "accessories");
    }

    /**
     * 新增附件
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增附件
     */
    @GetMapping("/upload")
    public String upload() {
        return prefix + "/upload";
    }

    /**
     * 新增保存附件
     */
    @RequiresPermissions("system:accessories:add")
    @Log(title = "附件", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysAccessories sysAccessories) {
        sysAccessories.setDeleted("0");//查询未删除的数据
        return toAjax(sysAccessoriesService.insertSysAccessories(sysAccessories));
    }

    /**
     * 新增保存附件
     */
    @Log(title = "附件", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult upload(@RequestParam("file") MultipartFile file) {
        try {
            String [] allowedFileExtensions = MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION;
            return AjaxResult.success(sysAccessoriesService.insertSysAccessories(file, allowedFileExtensions));
        } catch (InvalidExtensionException e) {
            e.printStackTrace();
            return AjaxResult.error("暂不支持其他类型文件");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("文件上传失败");
        }
    }

    /**
     * 修改附件
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        SysAccessories sysAccessories = sysAccessoriesService.selectSysAccessoriesById(id);
        mmap.put("sysAccessories", sysAccessories);
        return prefix + "/edit";
    }

    /**
     * 修改保存附件
     */
    @RequiresPermissions("system:accessories:edit")
    @Log(title = "附件", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysAccessories sysAccessories) {
        return toAjax(sysAccessoriesService.updateSysAccessories(sysAccessories));
    }

    /**
     * 删除附件
     */
    //@RequiresPermissions("system:accessories:remove")
    @Log(title = "附件", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysAccessoriesService.logicDeleteSysAccessoriesByIds(ids));
    }


    /**
     * 查询附件列表
     */
    //@RequiresPermissions("system:accessories:list")
    @PostMapping("/listByIds")
    @ResponseBody
    public TableDataInfo listByIds(String ids) {
        List<SysAccessories> list = sysAccessoriesService.selectSysAccessoriesByIds(ids);
        return getDataTable(list);
    }
    /**
     * 下载
     *
     * @param id
     * @param id
     * @param response
     */
    @GetMapping("/download/{id}")
    public void download(@PathVariable("id") Long id, HttpServletResponse response) {
        try {
            SysAccessories accessory = sysAccessoriesService.selectSysAccessoriesById(id);
            if (accessory != null) {
                logger.debug("正在下载文件"+accessory.toString());
                InputStream is;
                if(Global.isMinioFileStorageMode()) {
                    is = MinioClientUtils.getInstance().getObject(accessory.getPath());
                }else{
                    is = new FileInputStream(accessory.getPath());
                }
                // 设置response参数，可以打开下载页面
                response.reset();
                response.setContentType("application/octet-stream;charset=utf-8");
                response.setHeader("Content-Disposition", "attachment;filename="+ new String(accessory.getFileName().getBytes(), "iso-8859-1"));
                ServletOutputStream outputStream = response.getOutputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                BufferedOutputStream bos = new BufferedOutputStream(outputStream);
                byte[] buff = new byte[8192];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);

                }
                bis.close();
                bos.close();
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            logger.error("文件下载失败", e);
        }
    }

    /**
     * 图片下载
     * 
     * @param id
     * @param response
     */
    @GetMapping("/img/{id}")
    public void imageDownload(@PathVariable("id") Long id, HttpServletResponse response) {
        try {
            SysAccessories accessory = sysAccessoriesService.selectSysAccessoriesById(id);
            if (accessory != null) {
                if(accessory.getContent() != null && accessory.getContent().length > 0) {
                    response.getOutputStream().write(accessory.getContent());
                }else {
                    InputStream inputStream = MinioClientUtils.getInstance().getObject(accessory.getPath());
                    MinioClientUtils.getInstance().writeBytes(inputStream, response.getOutputStream());
                }
            }
        } catch (Exception e) {
            logger.error("获取图片失败", e);
        }
    }
}
