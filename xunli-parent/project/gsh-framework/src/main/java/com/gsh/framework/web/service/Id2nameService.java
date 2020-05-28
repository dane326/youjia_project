package com.gsh.framework.web.service;

import com.gsh.common.core.domain.AjaxResult;
import com.gsh.framework.util.Id2NameUtil;
import com.gsh.system.domain.SysDictData;
import com.gsh.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  html调用 thymeleaf 实现 id2name
 * 
 * @author gsh
 */
@Service("id2Name")
public class Id2nameService
{
    @Autowired
    private Id2NameUtil id2NameUtil;

    public String getNameByIds(String cls,String ids)
    {
        return id2NameUtil.getNameByIds(cls,ids);
    }
}
