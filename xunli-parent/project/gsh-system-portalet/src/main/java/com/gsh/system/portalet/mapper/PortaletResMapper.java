package com.gsh.system.portalet.mapper;

import com.gsh.system.portalet.domain.PortaletRes;
import java.util.List;

/**
 * 可配置资源信息Mapper接口
 * 
 * @author gsh
 * @date 2020-03-15
 */
public interface PortaletResMapper 
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
     * 删除可配置资源信息
     *
     * @param id 可配置资源信息ID
     * @return 结果
     */
    public int deletePortaletResById(Long id);

    /**
     * 批量删除可配置资源信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePortaletResByIds(String[] ids);

    /**
     *逻辑删除可配置资源信息
     *
     * @param id 可配置资源信息ID
     * @return 结果
     */
    public int logicDeletePortaletResById(Long id);

    /**
     * 批量逻辑删除可配置资源信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeletePortaletResByIds(String[] ids);
}
