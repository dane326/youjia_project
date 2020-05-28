package com.gsh.web.controller.system;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gsh.common.constant.UserConstants;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.utils.StringUtils;
import com.gsh.framework.shiro.service.SysRegisterService;
import com.gsh.system.domain.SysUser;
import com.gsh.system.service.ISysConfigService;
import com.gsh.system.service.ISysUserService;
import com.gsh.system.sms.service.ISysSmsRecordService;

/**
 * 注册
 *
 * @author gsh
 */
@Controller
public class SysRegisterController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysSmsRecordService sysSmsRecordService;
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @GetMapping("/register")
    public String register(HttpServletRequest request, HttpServletResponse response)
    {
        return "register";
    }
    @GetMapping("/resetPassword")
    public String resetPassword(HttpServletRequest request, HttpServletResponse response)
    {
        return "resetPassword";
    }
    @GetMapping("/getValidateCode")
    @ResponseBody
    public AjaxResult getValidateCode(HttpServletRequest request, HttpServletResponse response)
    {
        String codeType=request.getParameter("codeType");
        //发送验证码
        String mobile = request.getParameter("phonenumber");
        //正则验证手机号
        Pattern p= Pattern.compile("1\\d{10}");
        Matcher m=p.matcher(mobile);
        if(!m.matches()){
            return error("手机号格式错误");
        }
        //去数据库查是否存在这个手机号
        SysUser user = new SysUser();
        user.setPhonenumber(mobile);
        if ("register".equals(codeType)&&UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkPhoneUnique(user))){
            return error("账号已存在");
        }else if("reset".equals(codeType)&&!UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkPhoneUnique(user))){
            return error("账号不存在");
        }
        int code = (int)((Math.random()*9+1)*100000);
        int resule = sysSmsRecordService.sendSmsContent("sms_verify",mobile, String.valueOf(code));
        //把验证码放到session中
        //modify by wyk 2019-10-30,存入session的类型必须是String，不然前端注册校验出错
        request.getSession().setAttribute("validateCode",String.valueOf(code));

        return  toAjax(resule);
    }
    /*  这里使用FactoryInfo的注册方法
        @PostMapping("/register")
        @ResponseBody
        public AjaxResult ajaxRegister(@Validated SysUser user,HttpServletRequest request)
        {
            String code = request.getParameter("validateCode");
            String sessionCode = (String) StringUtils.nvl(request.getSession().getAttribute("validateCode"), "123456");
            if(StringUtils.isEmpty(code) || !code.equals(sessionCode)){
                return error("验证码错误");
            }

            if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName())))
            {
                return error("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
            }
            else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
            {
                return error("新增用户'" + user.getLoginName() + "'失败，手机号码已存在");
            }
            else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
            {
                return error("新增用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
            }
            //为用户赋予默认角色及权限
            user.setSalt(ShiroUtils.randomSalt());
            user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
            return toAjax(userService.insertUser(user, true));

        }*/
  /*  @GetMapping("/register")
    public String register()
    {
        return "register";
    }*/

    @PostMapping("/register")
    @ResponseBody
    public AjaxResult ajaxRegister(SysUser user)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
