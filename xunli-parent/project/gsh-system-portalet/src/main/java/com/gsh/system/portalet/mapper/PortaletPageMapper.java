package com.gsh.system.portalet.mapper;

import com.gsh.system.portalet.domain.PortaletPage;
import java.util.List;

/**
 * 自定义portal页面Mapper接口
 * 
 * @author gsh
 * @date 2020-03-17
 */
public interface PortaletPageMapper 
{
    /**
     * 查询自定义portal页面
     * 
     * @param id 自定义portal页面ID
     * @return 自定义portal页面
     */
    public PortaletPage selectPortaletPageById(Long id);

    /**
     * 查询自定义portal页面列表
     * 
     * @param portaletPage 自定义portal页面
     * @return 自定义portal页面集合
     */
    public List<PortaletPage> selectPortaletPageList(PortaletPage portaletPage);

    /**
     * 新增自定义portal页面
     * 
     * @param portaletPage 自定义portal页面
     * @return 结果
     */
    public int insertPortaletPage(PortaletPage portaletPage);

    /**
     * 修改自定义portal页面
     * 
     * @param portaletPage 自定义portal页面
     * @return 结果
     */
    public int updatePortaletPage(PortaletPage portaletPage);

    /**
     * 删除自定义portal页面
     *
     * @param id 自定义portal页面ID
     * @return 结果
     */
    public int deletePortaletPageById(Long id);

    /**
     * 批量删除自定义portal页面
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePortaletPageByIds(String[] ids);

    /**
     *逻辑删除自定义portal页面
     *
     * @param id 自定义portal页面ID
     * @return 结果
     */
    public int logicDeletePortaletPageById(Long id);

    /**
     * 批量逻辑删除自定义portal页面
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeletePortaletPageByIds(String[] ids);
    
    /**
	 * 获取模块下的最大排序号
	 * @param parentId
	 * @return
	 */
	public Long selectMaxSortNoByParentId(Long parentId);
}
