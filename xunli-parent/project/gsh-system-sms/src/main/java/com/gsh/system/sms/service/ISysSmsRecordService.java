package com.gsh.system.sms.service;

import com.gsh.system.sms.domain.SysSmsConfig;
import com.gsh.system.sms.domain.SysSmsRecord;

import java.util.List;

/**
 * 短信发送记录Service接口
 * 
 * @author gsh
 * @date 2019-09-04
 */
public interface ISysSmsRecordService {
	/**
	 * 查询短信发送记录
	 * 
	 * @param id
	 *            短信发送记录ID
	 * @return 短信发送记录
	 */
	public SysSmsRecord selectSysSmsRecordById(Long id);

	/**
	 * 查询短信发送记录列表
	 * 
	 * @param sysSmsRecord
	 *            短信发送记录
	 * @return 短信发送记录集合
	 */
	public List<SysSmsRecord> selectSysSmsRecordList(SysSmsRecord sysSmsRecord);

	/**
	 * 新增短信发送记录
	 * 
	 * @param sysSmsRecord
	 *            短信发送记录
	 * @return 结果
	 */
	public int insertSysSmsRecord(SysSmsRecord sysSmsRecord);

	/**
	 * 修改短信发送记录
	 * 
	 * @param sysSmsRecord
	 *            短信发送记录
	 * @return 结果
	 */
	public int updateSysSmsRecord(SysSmsRecord sysSmsRecord);

	/**
	 * 批量删除短信发送记录
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int deleteSysSmsRecordByIds(String ids);

	/**
	 * 删除短信发送记录信息
	 * 
	 * @param id
	 *            短信发送记录ID
	 * @return 结果
	 */
	public int deleteSysSmsRecordById(Long id);

	/**
	 * 批量逻辑删除短信发送记录
	 *
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int logicDeleteSysSmsRecordByIds(String ids);

	/**
	 * 逻辑删除短信发送记录信息
	 *
	 * @param id
	 *            短信发送记录ID
	 * @return 结果
	 */
	public int logicDeleteSysSmsRecordById(Long id);

	/**
	 * 发送短信
	 * @param smsType 
	 * @param smsContent 短信
	 * @param mobile 手机号
	 * @return
	 */
	public int sendSmsContent(String smsType, String mobile, String smsContent);

	/**
	 * 发送短信
	 * 
	 * @param sysSmsRecord
	 * @return
	 */
	public int sendSysSmsRecord(SysSmsRecord sysSmsRecord);

	/**
	 * 发送短信
	 * 
	 * @param sysSmsConfig
	 * @param sysSmsRecord
	 * @return
	 */
	public int sendSysSmsRecord(SysSmsConfig sysSmsConfig, SysSmsRecord sysSmsRecord);

}
