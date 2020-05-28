package com.gsh.web.controller.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.json.JSONObject;
import com.gsh.common.utils.ServletUtils;
import com.gsh.common.utils.StringUtils;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.SysUser;

/**
 * 登录验证
 * 
 * @author gsh
 */
@Controller
public class SysLoginController extends BaseController
{
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response)
    {
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request))
        {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }else if(ServletUtils.isAndroidRequest(request)){
            return ServletUtils.renderString(response, "{\"code\":\"session_time_out\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }

        return "login";


    }

    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe,String loginType,HttpServletRequest request,HttpServletResponse response)
    {
        //构建一个新的token来接受loginType
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        if (rememberMe){
            token.setRememberMe(rememberMe);
        }
        Subject subject = SecurityUtils.getSubject();
        try
        {
            subject.login(token);
            //手机登录成功后返回用户信息和头像地址
            if(ServletUtils.isAndroidRequest(request)){
                SysUser user= ShiroUtils.getSysUser();
                JSONObject jo=new JSONObject();
                jo.put("user",user);
                return new AjaxResult(AjaxResult.Type.SUCCESS,"登录成功",jo);
            }
            return success();
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }

    @GetMapping("/unauth")
    public String unauth()
    {
        return "error/unauth";
    }
}
