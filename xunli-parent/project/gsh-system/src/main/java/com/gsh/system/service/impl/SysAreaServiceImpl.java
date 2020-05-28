package com.gsh.system.service.impl;

import com.gsh.common.core.domain.Ztree;
import com.gsh.common.core.text.Convert;
import com.gsh.common.utils.DateUtils;
import com.gsh.common.utils.StringUtils;
import com.gsh.system.domain.SysArea;
import com.gsh.system.mapper.SysAreaMapper;
import com.gsh.system.service.ISysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 地域Service业务层处理
 * 
 * @author gsh-xrl
 * @date 2019-11-12
 */
@Service("area")
@Transactional
public class SysAreaServiceImpl implements ISysAreaService
{
    @Autowired
    private SysAreaMapper sysAreaMapper;
    /**
     * 查询地域
     * 
     * @param areaId 地域ID
     * @return 地域
     */
    @Override
    public SysArea selectSysAreaById(Long areaId)
    {

        return sysAreaMapper.selectSysAreaById(areaId);
    }

    /**
     * 查询地域列表
     * 
     * @param sysArea 地域
     * @return 地域
     */
    @Override
    public List<SysArea> selectSysAreaList(SysArea sysArea)
    {

        return sysAreaMapper.selectSysAreaList(sysArea);
    }

    /**
     * 新增地域
     * 
     * @param sysArea 地域
     * @return 结果
     */
    @Override
    public int insertSysArea(SysArea sysArea)
    {
        sysArea.setCreateTime(DateUtils.getNowDate());
        SysArea info = this.sysAreaMapper.selectSysAreaById(sysArea.getParentId());
        sysArea.setAncestors(info.getAncestors() + "," + sysArea.getParentId());
        return this.sysAreaMapper.insertSysArea(sysArea);
    }

    /**
     * 修改地域
     * 
     * @param sysArea 地域
     * @return 结果
     */
    @Override
    public int updateSysArea(SysArea sysArea)
    {
        sysArea.setUpdateTime(DateUtils.getNowDate());
        SysArea info = this.sysAreaMapper.selectSysAreaById(sysArea.getParentId());
        sysArea.setAncestors(info.getAncestors() + "," + sysArea.getParentId());
        return sysAreaMapper.updateSysArea(sysArea);
    }

    /**
     * 删除地域对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysAreaByIds(String ids)
    {
        return sysAreaMapper.deleteSysAreaByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除地域信息
     * 
     * @param areaId 地域ID
     * @return 结果
     */
    @Override
    public int deleteSysAreaById(Long areaId)
    {
        return sysAreaMapper.deleteSysAreaById(areaId);
    }

    /**
     * 查询地域树列表
     * 
     * @return 所有地域信息
     */
    @Override
    public List<Ztree> selectSysAreaTree()
    {
        List<SysArea> sysAreaList = sysAreaMapper.selectSysAreaList(new SysArea());
        List<Ztree> ztrees = new ArrayList<Ztree>();
        for (SysArea sysArea : sysAreaList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(sysArea.getAreaId());
            ztree.setpId(sysArea.getParentId());
            ztree.setName(sysArea.getAreaName());
            ztree.setTitle(sysArea.getAreaName());
            ztrees.add(ztree);
        }
        return ztrees;
    }

    @Override
    public String checkAreaNameUnique(SysArea sysArea) {
        Long deptId = StringUtils.isNull(sysArea.getAreaId()) ? -1L : sysArea.getAreaId();
        SysArea info = this.sysAreaMapper.checkAreaNameUnique(sysArea.getAreaName(), sysArea.getParentId());
        return StringUtils.isNotNull(info) && info.getAreaId() != deptId ? "1" : "0";
    }
}
