package com.gsh.web.controller.system;

import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.framework.util.Id2NameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * id2name
 * 
 * @author gsh
 */
@Controller
@RequestMapping("/system/id2name")
public class SysId2NameController extends BaseController
{
    @Autowired
    private Id2NameUtil id2NameUtil;

    @GetMapping("/getNameByIds")
    @ResponseBody
    public AjaxResult getNameByIds(String cls,String ids)
    {
        return AjaxResult.success(id2NameUtil.getNameByIds(cls,ids));
    }

}