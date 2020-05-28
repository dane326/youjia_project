package com.gsh.system.service.impl;

import java.util.List;

import com.gsh.common.annotation.DataScope;
import com.gsh.common.annotation.FactoryCode;
import com.gsh.common.utils.DateUtils;
import com.gsh.common.utils.spring.SpringUtils;
import com.gsh.system.annotation.DataTraces;
import com.gsh.system.domain.SysFactoryLabel;
import com.gsh.system.mapper.SysFactoryLabelMapper;
import com.gsh.system.service.ISysFactoryLabelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsh.common.core.text.Convert;

/**
 * 标签Service业务层处理
 *
 * @author gsh
 * @date 2019-10-17
 */
@Service
@DataTraces(objectClass = SysFactoryLabel.class, objectPkName = "lableId", updateOpDetailLevel = "all")
public class SysFactoryLabelServiceImpl implements ISysFactoryLabelService {

    @Autowired
    private SysFactoryLabelMapper sysFactoryLabelMapper;

    /**
     * 查询标签
     *
     * @param lableId 标签ID
     * @return 标签
     */
    @Override
    public SysFactoryLabel selectSysFactoryLabelById(Long lableId) {
        return sysFactoryLabelMapper.selectSysFactoryLabelById(lableId);
    }

    /**
     * 查询标签
     *
     * @param lableName   标签名称
     * @param factoryCode
     * @return 标签
     */
    @Override
    public SysFactoryLabel selectSysFactoryLabelByName(String lableName, Long factoryCode) {
        return sysFactoryLabelMapper.selectSysFactoryLabelByName(lableName, factoryCode);
    }

    /**
     * 查询标签列表
     *
     * @param sysFactoryLabel 标签
     * @return 标签
     */
    @Override
    @FactoryCode(tableAlias = {})
    @DataScope(deptAlias = "", userAlias = "u")
    public List<SysFactoryLabel> selectSysFactoryLabelList(SysFactoryLabel sysFactoryLabel) {
        return sysFactoryLabelMapper.selectSysFactoryLabelList(sysFactoryLabel);
    }

    /**
     * 新增标签
     *
     * @param sysFactoryLabel 标签
     * @return 结果
     */
    @Override
    public int insertSysFactoryLabel(SysFactoryLabel sysFactoryLabel) {
        sysFactoryLabel.setCreateTime(DateUtils.getNowDate());
        sysFactoryLabel.setStatus("1");
        return sysFactoryLabelMapper.insertSysFactoryLabel(sysFactoryLabel);
    }

    /**
     * 修改标签
     *
     * @param sysFactoryLabel 标签
     * @return 结果
     */
    @Override
    public int updateSysFactoryLabel(SysFactoryLabel sysFactoryLabel) {
        sysFactoryLabel.setUpdateTime(DateUtils.getNowDate());
        return sysFactoryLabelMapper.updateSysFactoryLabel(sysFactoryLabel);
    }

    /**
     * 删除标签对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysFactoryLabelByIds(String ids) {
        return sysFactoryLabelMapper.deleteSysFactoryLabelByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除标签信息
     *
     * @param lableId 标签ID
     * @return 结果
     */
    public int deleteSysFactoryLabelById(Long lableId) {
        return sysFactoryLabelMapper.deleteSysFactoryLabelById(lableId);
    }

    /**
     * 逻辑删除标签对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int logicDeleteSysFactoryLabelByIds(String ids) {
        return sysFactoryLabelMapper.logicDeleteSysFactoryLabelByIds(Convert.toStrArray(ids));
    }

    /**
     * 根据用户ID查询岗位
     *
     * @param userId 用户ID
     * @return 岗位列表
     */
    @Override
    @FactoryCode(tableAlias = {})
    public List<SysFactoryLabel> selectLabelsByUserId(Long userId) {
        List<SysFactoryLabel> userLables = sysFactoryLabelMapper.selectLabelsByUserId(userId);
        List<SysFactoryLabel> labels = SpringUtils.getAopProxy(this).selectSysFactoryLabelList(new SysFactoryLabel());
        for (SysFactoryLabel label : labels) {
            for (SysFactoryLabel userLable : userLables) {
                if (label.getLableId().longValue() == userLable.getLableId().longValue()) {
                    label.setFlag(true);
                    break;
                }
            }
        }
        return labels;
    }

    @Override
    public List<SysFactoryLabel> selectLabelsByPaperId(String paperid) {
        return sysFactoryLabelMapper.selectLabelsByPaperId(paperid);
    }

    /**
     * 逻辑删除标签信息
     *
     * @param lableId 标签ID
     * @return 结果
     */
    public int logicDeleteSysFactoryLabelById(Long lableId) {
        return sysFactoryLabelMapper.logicDeleteSysFactoryLabelById(lableId);
    }

}
