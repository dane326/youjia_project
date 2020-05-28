package com.gsh.system.sms.service.impl;

import com.gsh.common.core.text.Convert;
import com.gsh.common.utils.DateUtils;
import com.gsh.system.sms.domain.SysSmsConfig;
import com.gsh.system.sms.domain.SysSmsRecord;
import com.gsh.system.sms.mapper.SysSmsConfigMapper;
import com.gsh.system.sms.mapper.SysSmsRecordMapper;
import com.gsh.system.sms.service.ISysSmsRecordService;
import com.gsh.system.sms.utils.SmsSenderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * 短信发送记录Service业务层处理
 * 
 * @author gsh
 * @date 2019-09-04
 */
@Service
public class SysSmsRecordServiceImpl implements ISysSmsRecordService {
	
	private static final Logger logger = LoggerFactory.getLogger(SysSmsRecordServiceImpl.class);
	
	@Autowired
	private SysSmsRecordMapper sysSmsRecordMapper;

	@Autowired
	private SysSmsConfigMapper sysSmsConfigMapper;

	/**
	 * 查询短信发送记录
	 * 
	 * @param id
	 *            短信发送记录ID
	 * @return 短信发送记录
	 */
	@Override
	public SysSmsRecord selectSysSmsRecordById(Long id) {
		return sysSmsRecordMapper.selectSysSmsRecordById(id);
	}

	/**
	 * 查询短信发送记录列表
	 * 
	 * @param sysSmsRecord
	 *            短信发送记录
	 * @return 短信发送记录
	 */
	@Override
	public List<SysSmsRecord> selectSysSmsRecordList(SysSmsRecord sysSmsRecord) {
		return sysSmsRecordMapper.selectSysSmsRecordList(sysSmsRecord);
	}

	/**
	 * 新增短信发送记录
	 * 
	 * @param sysSmsRecord
	 *            短信发送记录
	 * @return 结果
	 */
	@Override
	public int insertSysSmsRecord(SysSmsRecord sysSmsRecord) {
		sysSmsRecord.setCreateTime(DateUtils.getNowDate());
		return sysSmsRecordMapper.insertSysSmsRecord(sysSmsRecord);
	}

	/**
	 * 修改短信发送记录
	 * 
	 * @param sysSmsRecord
	 *            短信发送记录
	 * @return 结果
	 */
	@Override
	public int updateSysSmsRecord(SysSmsRecord sysSmsRecord) {
		sysSmsRecord.setUpdateTime(DateUtils.getNowDate());
		return sysSmsRecordMapper.updateSysSmsRecord(sysSmsRecord);
	}

	/**
	 * 删除短信发送记录对象
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteSysSmsRecordByIds(String ids) {
		return sysSmsRecordMapper.deleteSysSmsRecordByIds(Convert.toStrArray(ids));
	}

	/**
	 * 删除短信发送记录信息
	 * 
	 * @param id
	 *            短信发送记录ID
	 * @return 结果
	 */
	@Override
	public int deleteSysSmsRecordById(Long id) {
		return sysSmsRecordMapper.deleteSysSmsRecordById(id);
	}

	/**
	 * 逻辑删除短信发送记录对象
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int logicDeleteSysSmsRecordByIds(String ids) {
		return sysSmsRecordMapper.logicDeleteSysSmsRecordByIds(Convert.toStrArray(ids));
	}

	/**
	 * 逻辑删除短信发送记录信息
	 *
	 * @param id
	 *            短信发送记录ID
	 * @return 结果
	 */
	@Override
	public int logicDeleteSysSmsRecordById(Long id) {
		return sysSmsRecordMapper.logicDeleteSysSmsRecordById(id);
	}
	
	/**
	 * 短信发送
	 *
	 * @param smsType
	 *            短信发送记录
	 * @return 结果
	 */
	@Override
	public int sendSmsContent(String smsType, String mobile,  String smsContent) {
		SysSmsRecord sysSmsRecord = new SysSmsRecord();
		sysSmsRecord.setReceiverMobile(mobile);
		sysSmsRecord.setSmsContent(smsContent);
		sysSmsRecord.setSmsType(smsType);
		return this.sendSysSmsRecord(sysSmsRecord);
	}

	/**
	 * 短信发送
	 *
	 * @param sysSmsRecord 
	 *            短信发送记录
	 * @return 结果
	 */
	@Override
	public int sendSysSmsRecord(SysSmsRecord sysSmsRecord) {
		SysSmsConfig sysSmsConfig = sysSmsConfigMapper.selectSysSmsConfigUsable(sysSmsRecord.getSmsType());
		return this.sendSysSmsRecord(sysSmsConfig, sysSmsRecord);
	}

	/**
	 * 短信发送
	 *
	 * @param sysSmsRecord 
	 *            短信发送记录
	 * @param sysSmsConfig 
	 *            短信发送配置
	 * @return 结果
	 */
	@Override
	public int sendSysSmsRecord(SysSmsConfig sysSmsConfig, SysSmsRecord sysSmsRecord) {
		try {
			List<Object[]> methodParams = new LinkedList<>();
			methodParams.add(new Object[] { sysSmsConfig, sysSmsConfig.getClass() });
			methodParams.add(new Object[] { sysSmsRecord, sysSmsRecord.getClass() });
			boolean result = SmsSenderUtils.invokeMethod(sysSmsConfig.getBeanName(), sysSmsConfig.getMethodName(), methodParams);
			if(result){
				sysSmsConfig.setSurplusNum(sysSmsConfig.getSurplusNum()-1);
				sysSmsConfigMapper.updateSysSmsConfig(sysSmsConfig);
				return sysSmsRecordMapper.insertSysSmsRecord(sysSmsRecord);
			}else {
				throw new Exception("短信接口失败");
			}
		} catch (Exception e) {
			logger.error("短信发送异常", e);
			return -1;
		}
	}

}
