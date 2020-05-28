package com.gsh.system.sms.mapper;

import com.gsh.system.sms.domain.SysSmsConfig;

import java.util.List;

/**
 * 短信接口配置Mapper接口
 * 
 * @author gsh
 * @date 2019-09-04
 */
public interface SysSmsConfigMapper {
	/**
	 * 查询短信接口配置
	 * 
	 * @param id
	 *            短信接口配置ID
	 * @return 短信接口配置
	 */
	public SysSmsConfig selectSysSmsConfigById(Long id);

	/**
	 * 查询短信接口配置列表
	 * 
	 * @param sysSmsConfig
	 *            短信接口配置
	 * @return 短信接口配置集合
	 */
	public List<SysSmsConfig> selectSysSmsConfigList(SysSmsConfig sysSmsConfig);

	/**
	 * 新增短信接口配置
	 * 
	 * @param sysSmsConfig
	 *            短信接口配置
	 * @return 结果
	 */
	public int insertSysSmsConfig(SysSmsConfig sysSmsConfig);

	/**
	 * 修改短信接口配置
	 * 
	 * @param sysSmsConfig
	 *            短信接口配置
	 * @return 结果
	 */
	public int updateSysSmsConfig(SysSmsConfig sysSmsConfig);

	/**
	 * 删除短信接口配置
	 *
	 * @param id
	 *            短信接口配置ID
	 * @return 结果
	 */
	public int deleteSysSmsConfigById(Long id);

	/**
	 * 批量删除短信接口配置
	 *
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int deleteSysSmsConfigByIds(String[] ids);

	/**
	 * 逻辑删除短信接口配置
	 *
	 * @param id
	 *            短信接口配置ID
	 * @return 结果
	 */
	public int logicDeleteSysSmsConfigById(Long id);

	/**
	 * 批量逻辑删除短信接口配置
	 *
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int logicDeleteSysSmsConfigByIds(String[] ids);

	/**
	 * selectSysSmsConfigUsable
	 * @param type
	 * @return
	 */
	public SysSmsConfig selectSysSmsConfigUsable(String type);
}
