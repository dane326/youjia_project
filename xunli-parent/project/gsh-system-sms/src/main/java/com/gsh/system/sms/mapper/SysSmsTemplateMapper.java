package com.gsh.system.sms.mapper;

import com.gsh.system.sms.domain.SysSmsTemplate;

import java.util.List;

/**
 * 短信模板配置Mapper接口
 * 
 * @author gsh
 * @date 2019-09-04
 */
public interface SysSmsTemplateMapper {
	/**
	 * 查询短信模板配置
	 * 
	 * @param id
	 *            短信模板配置ID
	 * @return 短信模板配置
	 */
	public SysSmsTemplate selectSysSmsTemplateById(Long id);

	/**
	 * 查询短信模板配置列表
	 * 
	 * @param sysSmsTemplate
	 *            短信模板配置
	 * @return 短信模板配置集合
	 */
	public List<SysSmsTemplate> selectSysSmsTemplateList(SysSmsTemplate sysSmsTemplate);

	/**
	 * 新增短信模板配置
	 * 
	 * @param sysSmsTemplate
	 *            短信模板配置
	 * @return 结果
	 */
	public int insertSysSmsTemplate(SysSmsTemplate sysSmsTemplate);

	/**
	 * 修改短信模板配置
	 * 
	 * @param sysSmsTemplate
	 *            短信模板配置
	 * @return 结果
	 */
	public int updateSysSmsTemplate(SysSmsTemplate sysSmsTemplate);

	/**
	 * 删除短信模板配置
	 *
	 * @param id
	 *            短信模板配置ID
	 * @return 结果
	 */
	public int deleteSysSmsTemplateById(Long id);

	/**
	 * 批量删除短信模板配置
	 *
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int deleteSysSmsTemplateByIds(String[] ids);

	/**
	 * 逻辑删除短信模板配置
	 *
	 * @param id
	 *            短信模板配置ID
	 * @return 结果
	 */
	public int logicDeleteSysSmsTemplateById(Long id);

	/**
	 * 批量逻辑删除短信模板配置
	 *
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int logicDeleteSysSmsTemplateByIds(String[] ids);
}
