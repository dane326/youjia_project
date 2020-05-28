package com.gsh.web.controller.system;

import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.xunli.config.XGlobleConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2020/1/13 0013.
 */
@Controller
@RequestMapping("/system/globle")
public class SysGlobleController extends BaseController {
    @PostMapping("/checkLicense")
    @ResponseBody
    public AjaxResult checkLicense(){
        Object o = XGlobleConfig.getConfig("V");

        return toAjax("200".equals(o));
    }
}
