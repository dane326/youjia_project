package com.gsh.system.portalet.service;

import java.util.List;

import com.gsh.system.portalet.domain.PortaletPage;

/**
 * 自定义portal页面Service接口
 * 
 * @author gsh
 * @date 2020-03-17
 */
public interface IPortaletPageService {
	/**
	 * 查询自定义portal页面
	 * 
	 * @param id
	 *            自定义portal页面ID
	 * @return 自定义portal页面
	 */
	public PortaletPage selectPortaletPageById(Long id);

	/**
	 * 查询自定义portal页面列表
	 * 
	 * @param portaletPage
	 *            自定义portal页面
	 * @return 自定义portal页面集合
	 */
	public List<PortaletPage> selectPortaletPageList(PortaletPage portaletPage);

	/**
	 * 新增自定义portal页面
	 * 
	 * @param portaletPage
	 *            自定义portal页面
	 * @return 结果
	 */
	public int insertPortaletPage(PortaletPage portaletPage);

	/**
	 * 修改自定义portal页面
	 * 
	 * @param portaletPage
	 *            自定义portal页面
	 * @return 结果
	 */
	public int updatePortaletPage(PortaletPage portaletPage);
	
	/**
	 * 修改自定义portal页面
	 * 
	 * @param portaletPages
	 *            自定义portal页面
	 * @return 结果
	 */
	public int updatePortaletPages(List<PortaletPage> portaletPages);

	/**
	 * 批量删除自定义portal页面
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int deletePortaletPageByIds(String ids);

	/**
	 * 删除自定义portal页面信息
	 * 
	 * @param id
	 *            自定义portal页面ID
	 * @return 结果
	 */
	public int deletePortaletPageById(Long id);

	/**
	 * 批量逻辑删除自定义portal页面
	 *
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int logicDeletePortaletPageByIds(String ids);

	/**
	 * 逻辑删除自定义portal页面信息
	 *
	 * @param id
	 *            自定义portal页面ID
	 * @return 结果
	 */
	public int logicDeletePortaletPageById(Long id);

	/**
	 * 增加资源
	 * @param modulePageId
	 * @param ids
	 * @return
	 */
	public int insertResByIds(Long modulePageId, String ids);

	/**
	 * 获取模块下的最大排序号
	 * @param parentId
	 * @return
	 */
	public Long selectMaxSortNoByParentId(Long parentId);

}
