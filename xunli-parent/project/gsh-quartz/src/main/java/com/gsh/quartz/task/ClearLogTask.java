package com.gsh.quartz.task;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Created by wuyingkai on 2019/12/16.
 */
@Component("clearLogTask")
public class ClearLogTask {
    //清理当前服务器下的一个月前的日志

    public void clearLog(){
        //获取当前日志的存储绝对日志
        File file = new File("");
        String path =file.getAbsolutePath()+"/logs";
        //删除当前路径下的超过30天以上的文件
        // 系统日志的 命名 sys-info.%d{yyyy-MM-dd}.log
        //错误日志的命名 sys-error.%d{yyyy-MM-dd}.log
        String[] cmd = new String[] { "/bin/sh", "-c", "find "+path+" -mtime +31 -name \"sys-*.log\" -exec rm -rf {} \\;" };
        try {
            Process process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("清理一个月前的日志成功");
    }

}
