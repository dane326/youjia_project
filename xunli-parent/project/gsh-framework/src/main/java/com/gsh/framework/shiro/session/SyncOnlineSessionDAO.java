package com.gsh.framework.shiro.session;

/**
 * 同步onlinesession
 * 
 * @author gsh
 */
public interface SyncOnlineSessionDAO {
    /**
     * 更新会话；如更新会话最后访问时间/停止会话/设置超时时间/设置移除属性等会调用
     */
    public void syncToDb(OnlineSession onlineSession);

}
