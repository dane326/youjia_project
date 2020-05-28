package com.gsh.system.sms.mapper;

import com.gsh.system.sms.domain.SysSmsRecord;

import java.util.List;

/**
 * 短信发送记录Mapper接口
 * 
 * @author gsh
 * @date 2019-09-04
 */
public interface SysSmsRecordMapper {
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
	 * 删除短信发送记录
	 *
	 * @param id
	 *            短信发送记录ID
	 * @return 结果
	 */
	public int deleteSysSmsRecordById(Long id);

	/**
	 * 批量删除短信发送记录
	 *
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int deleteSysSmsRecordByIds(String[] ids);

	/**
	 * 逻辑删除短信发送记录
	 *
	 * @param id
	 *            短信发送记录ID
	 * @return 结果
	 */
	public int logicDeleteSysSmsRecordById(Long id);

	/**
	 * 批量逻辑删除短信发送记录
	 *
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	public int logicDeleteSysSmsRecordByIds(String[] ids);
}
