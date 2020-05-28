package com.gsh.system.portalet.service;

import java.util.List;

import com.gsh.system.portalet.domain.PortaletUser;

/**
 * 用户配置的资源信息Service接口
 * 
 * @author gsh
 * @date 2020-03-17
 */
public interface IPortaletUserService 
{
    /**
     * 查询用户配置的资源信息
     * 
     * @param id 用户配置的资源信息ID
     * @return 用户配置的资源信息
     */
    public PortaletUser selectPortaletUserById(Long id);

    /**
     * 查询用户配置的资源信息列表
     * 
     * @param portaletUser 用户配置的资源信息
     * @return 用户配置的资源信息集合
     */
    public List<PortaletUser> selectPortaletUserList(PortaletUser portaletUser);

    /**
     * 新增用户配置的资源信息
     * 
     * @param portaletUser 用户配置的资源信息
     * @return 结果
     */
    public int insertPortaletUser(PortaletUser portaletUser);

    /**
     * 修改用户配置的资源信息
     * 
     * @param portaletUser 用户配置的资源信息
     * @return 结果
     */
    public int updatePortaletUser(PortaletUser portaletUser);
    
    /**
	 * 修改用户配置的资源信息
	 * 
	 * @param portaletUsers 用户配置的资源信息
	 * @return 结果
	 */
	public int updatePortaletUsers(List<PortaletUser> portaletUsers);

    /**
     * 批量删除用户配置的资源信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePortaletUserByIds(String ids);

    /**
     * 删除用户配置的资源信息信息
     * 
     * @param id 用户配置的资源信息ID
     * @return 结果
     */
    public int deletePortaletUserById(Long id);
    /**
     * 批量逻辑删除用户配置的资源信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeletePortaletUserByIds(String ids);

    /**
     * 逻辑删除用户配置的资源信息信息
     *
     * @param id 用户配置的资源信息ID
     * @return 结果
     */
    public int logicDeletePortaletUserById(Long id);

    /**
	 * 增加资源
	 * @param userCode
	 * @param ids
	 * @return
	 */
	public int insertResByIds(String userCode, String ids);

	/**
	 * 获取用户下的最大排序号
	 * @param userCode
	 * @return
	 */
	public Long selectMaxSortNoByUserCode(String userCode);
}
