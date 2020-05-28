package com.gsh.system.sms.service.impl;

import com.gsh.common.core.text.Convert;
import com.gsh.common.utils.DateUtils;
import com.gsh.system.sms.domain.SysSmsConfig;
import com.gsh.system.sms.domain.SysSmsRecord;
import com.gsh.system.sms.mapper.SysSmsConfigMapper;
import com.gsh.system.sms.mapper.SysSmsRecordMapper;
import com.gsh.system.sms.service.ISysSmsConfigService;
import com.gsh.system.sms.service.ISysSmsRecordService;
import com.gsh.system.sms.service.ISysSmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短信接口配置Service业务层处理
 * 
 * @author gsh
 * @date 2019-09-04
 */
@Service
public class SysSmsConfigServiceImpl implements ISysSmsConfigService {
	@Autowired
	private SysSmsConfigMapper sysSmsConfigMapper;

	@Autowired
	private SysSmsRecordMapper sysSmsRecordMapper;

	@Autowired
	private ISysSmsTemplateService sysSmsTemplateService;

	@Autowired
	private ISysSmsRecordService sysSmsRecordService;

	/**
	 * 查询短信接口配置
	 * 
	 * @param id
	 *            短信接口配置ID
	 * @return 短信接口配置
	 */
	@Override
	public SysSmsConfig selectSysSmsConfigById(Long id) {
		return sysSmsConfigMapper.selectSysSmsConfigById(id);
	}

	/**
	 * 查询短信接口配置列表
	 * 
	 * @param sysSmsConfig
	 *            短信接口配置
	 * @return 短信接口配置
	 */
	@Override
	public List<SysSmsConfig> selectSysSmsConfigList(SysSmsConfig sysSmsConfig) {
		return sysSmsConfigMapper.selectSysSmsConfigList(sysSmsConfig);
	}

	/**
	 * 新增短信接口配置
	 * 
	 * @param sysSmsConfig
	 *            短信接口配置
	 * @return 结果
	 */
	@Override
	public int insertSysSmsConfig(SysSmsConfig sysSmsConfig) {
		sysSmsConfig.setCreateTime(DateUtils.getNowDate());
		return sysSmsConfigMapper.insertSysSmsConfig(sysSmsConfig);
	}

	/**
	 * 修改短信接口配置
	 * 
	 * @param sysSmsConfig
	 *            短信接口配置
	 * @return 结果
	 */
	@Override
	public int updateSysSmsConfig(SysSmsConfig sysSmsConfig) {
		sysSmsConfig.setUpdateTime(DateUtils.getNowDate());
		return sysSmsConfigMapper.updateSysSmsConfig(sysSmsConfig);
	}

	/**
	 * 删除短信接口配置对象
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteSysSmsConfigByIds(String ids) {
		return sysSmsConfigMapper.deleteSysSmsConfigByIds(Convert.toStrArray(ids));
	}

	/**
	 * 删除短信接口配置信息
	 * 
	 * @param id
	 *            短信接口配置ID
	 * @return 结果
	 */
	@Override
	public int deleteSysSmsConfigById(Long id) {
		return sysSmsConfigMapper.deleteSysSmsConfigById(id);
	}

	/**
	 * 逻辑删除短信接口配置对象
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int logicDeleteSysSmsConfigByIds(String ids) {
		return sysSmsConfigMapper.logicDeleteSysSmsConfigByIds(Convert.toStrArray(ids));
	}

	/**
	 * 逻辑删除短信接口配置信息
	 *
	 * @param id
	 *            短信接口配置ID
	 * @return 结果
	 */
	@Override
	public int logicDeleteSysSmsConfigById(Long id) {
		return sysSmsConfigMapper.logicDeleteSysSmsConfigById(id);
	}

	@Override
	public int triySysSmsConfig(Long id, SysSmsRecord sysSmsRecord) {
		SysSmsConfig sysSmsConfig = this.selectSysSmsConfigById(id);
		return sysSmsRecordService.sendSysSmsRecord(sysSmsConfig, sysSmsRecord);
	}

}
