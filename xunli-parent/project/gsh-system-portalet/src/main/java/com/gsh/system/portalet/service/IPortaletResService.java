package com.gsh.system.portalet.service;

import com.gsh.common.core.domain.Ztree;
import com.gsh.system.portalet.domain.PortaletRes;
import java.util.List;

/**
 * 可配置资源信息Service接口
 * 
 * @author gsh
 * @date 2020-03-15
 */
public interface IPortaletResService 
{
    /**
     * 查询可配置资源信息
     * 
     * @param id 可配置资源信息ID
     * @return 可配置资源信息
     */
    public PortaletRes selectPortaletResById(Long id);

    /**
     * 查询可配置资源信息列表
     * 
     * @param portaletRes 可配置资源信息
     * @return 可配置资源信息集合
     */
    public List<PortaletRes> selectPortaletResList(PortaletRes portaletRes);

    /**
     * 新增可配置资源信息
     * 
     * @param portaletRes 可配置资源信息
     * @return 结果
     */
    public int insertPortaletRes(PortaletRes portaletRes);

    /**
     * 修改可配置资源信息
     * 
     * @param portaletRes 可配置资源信息
     * @return 结果
     */
    public int updatePortaletRes(PortaletRes portaletRes);

    /**
     * 批量删除可配置资源信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePortaletResByIds(String ids);

    /**
     * 删除可配置资源信息信息
     * 
     * @param id 可配置资源信息ID
     * @return 结果
     */
    public int deletePortaletResById(Long id);
    /**
     * 批量逻辑删除可配置资源信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeletePortaletResByIds(String ids);

    /**
     * 逻辑删除可配置资源信息信息
     *
     * @param id 可配置资源信息ID
     * @return 结果
     */
    public int logicDeletePortaletResById(Long id);

    /**
     * 加载资源列表树
     * @param portaletRes
     * @param parentNoCheckFlag
     * @return
     */
	public List<Ztree> selectResTree(PortaletRes portaletRes, Boolean parentNoCheckFlag);

}
