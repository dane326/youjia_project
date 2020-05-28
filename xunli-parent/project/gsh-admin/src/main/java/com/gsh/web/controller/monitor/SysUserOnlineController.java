package com.gsh.web.controller.monitor;

import com.gsh.common.annotation.Log;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.enums.OnlineStatus;
import com.gsh.framework.redis.service.RedisUserOnlineService;
import com.gsh.framework.shiro.session.OnlineSession;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.SysUserOnline;
import com.gsh.system.service.ISysUserOnlineService;
import com.gsh.system.utils.FactoryUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 在线用户监控
 *
 * @author gsh
 */
@Controller
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {
    private String prefix = "monitor/online";

    @Autowired
    private ISysUserOnlineService userOnlineService;

    @Autowired
    private AbstractSessionDAO onlineSessionDAO;

    @Autowired
    private RedisUserOnlineService redisUserOnlineService;

    // session存储位置
    @Value("${gsh.onlineSessionStore}")
    private String sessionStore;

    @RequiresPermissions("monitor:online:view")
    @GetMapping()
    public String online(ModelMap mmap) {
        mmap.put("factoryCode", FactoryUtils.getFactoryCode());
        if(FactoryUtils.getFactoryTreeMode()){
            return prefix + "/onlinetree";
        }
        return prefix + "/online";
    }

    @RequiresPermissions("monitor:online:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUserOnline userOnline) {
        List<SysUserOnline> list = null;
        //默认只查询当前用户所属factory的在线用户
        if ("redis".equals(sessionStore)) {
            list = redisUserOnlineService.getOnlineUsers(userOnline);
        } else {
            startPage();
            list = userOnlineService.selectUserOnlineList(userOnline);
        }
        return getDataTable(list);
    }

    @RequiresPermissions("monitor:online:batchForceLogout")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @PostMapping("/batchForceLogout")
    @ResponseBody
    public AjaxResult batchForceLogout(@RequestParam("ids[]") String[] ids) {
        for (String sessionId : ids) {
            SysUserOnline online = null;
            if ("redis".equals(sessionStore)) {
                online = redisUserOnlineService.selectOnlineById(sessionId);
            } else {
                online = userOnlineService.selectOnlineById(sessionId);
            }
            if (online == null) {
                return error("用户已下线");
            }
            if ("redis".equals(sessionStore)) {
                redisUserOnlineService.forceLogout(sessionId);
            } else {
                OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
                if (onlineSession == null) {
                    return error("用户已下线");
                }
                onlineSession.setStatus(OnlineStatus.off_line);
                onlineSessionDAO.update(onlineSession);
                online.setStatus(OnlineStatus.off_line);
                userOnlineService.saveOnline(online);
            }
        }
        return success();
    }

    @RequiresPermissions("monitor:online:forceLogout")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @PostMapping("/forceLogout")
    @ResponseBody
    public AjaxResult forceLogout(String sessionId) {
        SysUserOnline online = null;
        if ("redis".equals(sessionStore)) {
            online = redisUserOnlineService.selectOnlineById(sessionId);
        } else {
            online = userOnlineService.selectOnlineById(sessionId);
        }
        if (sessionId.equals(ShiroUtils.getSessionId())) {
            return error("当前登陆用户无法强退");
        }
        if (online == null) {
            return error("用户已下线");
        }
        if ("redis".equals(sessionStore)) {
            redisUserOnlineService.forceLogout(sessionId);
        } else {
            OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
            if (onlineSession == null) {
                return error("用户已下线");
            }
            onlineSession.setStatus(OnlineStatus.off_line);
            onlineSessionDAO.update(onlineSession);
            online.setStatus(OnlineStatus.off_line);
            userOnlineService.saveOnline(online);
        }
        return success();
    }
}
