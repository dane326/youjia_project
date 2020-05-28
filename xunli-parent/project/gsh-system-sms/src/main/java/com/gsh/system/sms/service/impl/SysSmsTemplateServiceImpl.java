package com.gsh.system.sms.service.impl;

import com.gsh.common.core.text.Convert;
import com.gsh.common.utils.DateUtils;
import com.gsh.system.sms.domain.SysSmsTemplate;
import com.gsh.system.sms.mapper.SysSmsTemplateMapper;
import com.gsh.system.sms.service.ISysSmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短信模板配置Service业务层处理
 * 
 * @author gsh
 * @date 2019-09-04
 */
@Service
public class SysSmsTemplateServiceImpl implements ISysSmsTemplateService {
	@Autowired
	private SysSmsTemplateMapper sysSmsTemplateMapper;

	/**
	 * 查询短信模板配置
	 * 
	 * @param id
	 *            短信模板配置ID
	 * @return 短信模板配置
	 */
	@Override
	public SysSmsTemplate selectSysSmsTemplateById(Long id) {
		return sysSmsTemplateMapper.selectSysSmsTemplateById(id);
	}

	/**
	 * 查询短信模板配置列表
	 * 
	 * @param sysSmsTemplate
	 *            短信模板配置
	 * @return 短信模板配置
	 */
	@Override
	public List<SysSmsTemplate> selectSysSmsTemplateList(SysSmsTemplate sysSmsTemplate) {
		return sysSmsTemplateMapper.selectSysSmsTemplateList(sysSmsTemplate);
	}

	/**
	 * 新增短信模板配置
	 * 
	 * @param sysSmsTemplate
	 *            短信模板配置
	 * @return 结果
	 */
	@Override
	public int insertSysSmsTemplate(SysSmsTemplate sysSmsTemplate) {
		sysSmsTemplate.setCreateTime(DateUtils.getNowDate());
		return sysSmsTemplateMapper.insertSysSmsTemplate(sysSmsTemplate);
	}

	/**
	 * 修改短信模板配置
	 * 
	 * @param sysSmsTemplate
	 *            短信模板配置
	 * @return 结果
	 */
	@Override
	public int updateSysSmsTemplate(SysSmsTemplate sysSmsTemplate) {
		sysSmsTemplate.setUpdateTime(DateUtils.getNowDate());
		return sysSmsTemplateMapper.updateSysSmsTemplate(sysSmsTemplate);
	}

	/**
	 * 删除短信模板配置对象
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteSysSmsTemplateByIds(String ids) {
		return sysSmsTemplateMapper.deleteSysSmsTemplateByIds(Convert.toStrArray(ids));
	}

	/**
	 * 删除短信模板配置信息
	 * 
	 * @param id
	 *            短信模板配置ID
	 * @return 结果
	 */
	@Override
	public int deleteSysSmsTemplateById(Long id) {
		return sysSmsTemplateMapper.deleteSysSmsTemplateById(id);
	}

	/**
	 * 逻辑删除短信模板配置对象
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int logicDeleteSysSmsTemplateByIds(String ids) {
		return sysSmsTemplateMapper.logicDeleteSysSmsTemplateByIds(Convert.toStrArray(ids));
	}

	/**
	 * 逻辑删除短信模板配置信息
	 *
	 * @param id
	 *            短信模板配置ID
	 * @return 结果
	 */
	@Override
	public int logicDeleteSysSmsTemplateById(Long id) {
		return sysSmsTemplateMapper.logicDeleteSysSmsTemplateById(id);
	}

}
