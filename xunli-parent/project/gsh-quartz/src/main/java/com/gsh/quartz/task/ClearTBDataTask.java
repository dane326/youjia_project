package com.gsh.quartz.task;

import com.gsh.quartz.service.ISystemJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2020/1/9 0009.
 */
@Component("clearTBDataTask")
public class ClearTBDataTask {
    @Autowired
    private ISystemJobService systemJobService;
    public void clearTBData(){
        systemJobService.clearSystemTableData("sys_job_log","1","MONTH");
        System.out.println("清理一个月前的日志成功");
    }
}
