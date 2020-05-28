package com.gsh.system.portalet.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsh.common.core.text.Convert;
import com.gsh.common.utils.DateUtils;
import com.gsh.system.portalet.domain.PortaletRes;
import com.gsh.system.portalet.domain.PortaletUser;
import com.gsh.system.portalet.mapper.PortaletResMapper;
import com.gsh.system.portalet.mapper.PortaletUserMapper;
import com.gsh.system.portalet.service.IPortaletUserService;

/**
 * 用户配置的资源信息Service业务层处理
 * 
 * @author gsh
 * @date 2020-03-17
 */
@Service
public class PortaletUserServiceImpl implements IPortaletUserService {
	@Autowired
	private PortaletUserMapper portaletUserMapper;
	
	@Autowired
	private PortaletResMapper portaletResMapper;

	/**
	 * 查询用户配置的资源信息
	 * 
	 * @param id
	 *            用户配置的资源信息ID
	 * @return 用户配置的资源信息
	 */
	@Override
	public PortaletUser selectPortaletUserById(Long id) {
		return portaletUserMapper.selectPortaletUserById(id);
	}

	/**
	 * 查询用户配置的资源信息列表
	 * 
	 * @param portaletUser
	 *            用户配置的资源信息
	 * @return 用户配置的资源信息
	 */
	@Override
	public List<PortaletUser> selectPortaletUserList(PortaletUser portaletUser) {
		return portaletUserMapper.selectPortaletUserList(portaletUser);
	}

	/**
	 * 新增用户配置的资源信息
	 * 
	 * @param portaletUser
	 *            用户配置的资源信息
	 * @return 结果
	 */
	@Override
	public int insertPortaletUser(PortaletUser portaletUser) {
		portaletUser.setCreateTime(DateUtils.getNowDate());
		return portaletUserMapper.insertPortaletUser(portaletUser);
	}

	/**
	 * 修改用户配置的资源信息
	 * 
	 * @param portaletUser
	 *            用户配置的资源信息
	 * @return 结果
	 */
	@Override
	public int updatePortaletUser(PortaletUser portaletUser) {
		portaletUser.setUpdateTime(DateUtils.getNowDate());
		return portaletUserMapper.updatePortaletUser(portaletUser);
	}

	/**
	 * 删除用户配置的资源信息对象
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deletePortaletUserByIds(String ids) {
		return portaletUserMapper.deletePortaletUserByIds(Convert.toStrArray(ids));
	}

	/**
	 * 删除用户配置的资源信息信息
	 * 
	 * @param id
	 *            用户配置的资源信息ID
	 * @return 结果
	 */
	@Override
	public int deletePortaletUserById(Long id) {
		return portaletUserMapper.deletePortaletUserById(id);
	}

	/**
	 * 逻辑删除用户配置的资源信息对象
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int logicDeletePortaletUserByIds(String ids) {
		return portaletUserMapper.logicDeletePortaletUserByIds(Convert.toStrArray(ids));
	}

	/**
	 * 逻辑删除用户配置的资源信息信息
	 *
	 * @param id
	 *            用户配置的资源信息ID
	 * @return 结果
	 */
	public int logicDeletePortaletUserById(Long id) {
		return portaletUserMapper.logicDeletePortaletUserById(id);
	}

	@Override
	public int updatePortaletUsers(List<PortaletUser> portaletUsers) {
		if(CollectionUtils.isNotEmpty(portaletUsers)){
			for(PortaletUser portaletUser : portaletUsers){
				portaletUser.setUpdateTime(DateUtils.getNowDate());
				portaletUserMapper.updatePortaletUser(portaletUser);
			}
			
			return 1;
		}
		
		return 0;
	}

	@Override
	public int insertResByIds(String userCode, String ids) {
		if (StringUtils.isNotBlank(ids)) {
			// 获取资源最大sortNo
			Long sortNo = this.selectMaxSortNoByUserCode(userCode);
			sortNo = (sortNo == null ? 0 : sortNo) + 1;
			String[] idList = StringUtils.split(ids, ",");
			for (int ix = 0; ix < idList.length; ix++) {
				String id = idList[ix];
				PortaletRes res = portaletResMapper.selectPortaletResById(Long.valueOf(id));
				PortaletUser portaletUser = new PortaletUser();
				portaletUser.setSystemCode(res.getSystemCode());
				portaletUser.setSystemName(res.getSystemName());
				portaletUser.setModuleCode(res.getModuleCode());
				portaletUser.setModuleName(res.getModuleName());
				portaletUser.setUserCode(userCode);
				portaletUser.setResId(res.getId());
				portaletUser.setWidth(res.getWidth());
				portaletUser.setHeight(res.getHeight());
				portaletUser.setDeleted(res.getDeleted());
				portaletUser.setSortNo(sortNo + ix);
				portaletUserMapper.insertPortaletUser(portaletUser);
			}
		}

		return 1;
	}

	@Override
	public Long selectMaxSortNoByUserCode(String userCode) {
		return portaletUserMapper.selectMaxSortNoByUserCode(userCode);
	}

}
