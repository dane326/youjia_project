package com.gsh.system.portalet.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsh.common.core.domain.Ztree;
import com.gsh.common.core.text.Convert;
import com.gsh.common.utils.DateUtils;
import com.gsh.system.portalet.domain.PortaletRes;
import com.gsh.system.portalet.mapper.PortaletResMapper;
import com.gsh.system.portalet.service.IPortaletResService;

/**
 * 可配置资源信息Service业务层处理
 * 
 * @author gsh
 * @date 2020-03-15
 */
@Service
public class PortaletResServiceImpl implements IPortaletResService {
	@Autowired
	private PortaletResMapper portaletResMapper;

	/**
	 * 查询可配置资源信息
	 * 
	 * @param id
	 *            可配置资源信息ID
	 * @return 可配置资源信息
	 */
	@Override
	public PortaletRes selectPortaletResById(Long id) {
		return portaletResMapper.selectPortaletResById(id);
	}

	/**
	 * 查询可配置资源信息列表
	 * 
	 * @param portaletRes
	 *            可配置资源信息
	 * @return 可配置资源信息
	 */
	@Override
	public List<PortaletRes> selectPortaletResList(PortaletRes portaletRes) {
		return portaletResMapper.selectPortaletResList(portaletRes);
	}

	/**
	 * 新增可配置资源信息
	 * 
	 * @param portaletRes
	 *            可配置资源信息
	 * @return 结果
	 */
	@Override
	public int insertPortaletRes(PortaletRes portaletRes) {
		portaletRes.setCreateTime(DateUtils.getNowDate());
		return portaletResMapper.insertPortaletRes(portaletRes);
	}

	/**
	 * 修改可配置资源信息
	 * 
	 * @param portaletRes
	 *            可配置资源信息
	 * @return 结果
	 */
	@Override
	public int updatePortaletRes(PortaletRes portaletRes) {
		portaletRes.setUpdateTime(DateUtils.getNowDate());
		if ("sys".equals(portaletRes.getResType())) {
			PortaletRes curRes = portaletResMapper.selectPortaletResById(portaletRes.getId());
			if (!curRes.getSystemCode().equals(portaletRes.getSystemCode())
					|| curRes.getSystemName().equals(portaletRes.getSystemName())) {
				PortaletRes conRes = new PortaletRes();
				conRes.setSystemCode(curRes.getSystemCode());
				// 更新所有子节点
				List<PortaletRes> ress = portaletResMapper.selectPortaletResList(conRes);
				for (PortaletRes res : ress) {
					if(!res.getId().equals(portaletRes.getId())){
						res.setSystemName(portaletRes.getSystemName());
						res.setSystemCode(portaletRes.getSystemCode());
						portaletResMapper.updatePortaletRes(res);
					}
				}
			}
		} else if ("module".equals(portaletRes.getResType())) {
			PortaletRes curRes = portaletResMapper.selectPortaletResById(portaletRes.getId());
			if (!curRes.getModuleCode().equals(portaletRes.getModuleCode())
					|| curRes.getModuleName().equals(portaletRes.getModuleName())) {
				// 更新所有的子节点
				PortaletRes conRes = new PortaletRes();
				conRes.setParentId(portaletRes.getId());
				// 更新所有子节点
				List<PortaletRes> ress = portaletResMapper.selectPortaletResList(conRes);
				for (PortaletRes res : ress) {
					res.setModuleName(portaletRes.getModuleName());
					res.setModuleCode(portaletRes.getModuleCode());
					portaletResMapper.updatePortaletRes(res);
				}
			}
		}

		return portaletResMapper.updatePortaletRes(portaletRes);
	}

	/**
	 * 删除可配置资源信息对象
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deletePortaletResByIds(String ids) {
		return portaletResMapper.deletePortaletResByIds(Convert.toStrArray(ids));
	}

	/**
	 * 删除可配置资源信息信息
	 * 
	 * @param id
	 *            可配置资源信息ID
	 * @return 结果
	 */
	@Override
	public int deletePortaletResById(Long id) {
		return portaletResMapper.deletePortaletResById(id);
	}

	/**
	 * 逻辑删除可配置资源信息对象
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int logicDeletePortaletResByIds(String ids) {
		return portaletResMapper.logicDeletePortaletResByIds(Convert.toStrArray(ids));
	}

	/**
	 * 逻辑删除可配置资源信息信息
	 *
	 * @param id
	 *            可配置资源信息ID
	 * @return 结果
	 */
	public int logicDeletePortaletResById(Long id) {
		return portaletResMapper.logicDeletePortaletResById(id);
	}

	/**
	 * 加载资源列表树
	 * 
	 * @param portaletRes
	 * @param parentNoCheckFlag
	 * @return
	 */
	@Override
	public List<Ztree> selectResTree(PortaletRes portaletRes, Boolean parentNoCheckFlag) {
		List<PortaletRes> resList = portaletResMapper.selectPortaletResList(portaletRes);
		List<Ztree> ztrees = initZtree(resList, parentNoCheckFlag);
		return ztrees;
	}

	/**
	 * 对象转资源树
	 *
	 * @param resList
	 * @param parentNoCheckFlag
	 * @return 树结构列表
	 */
	public List<Ztree> initZtree(List<PortaletRes> resList, Boolean parentNoCheckFlag) {
		List<Ztree> ztrees = new ArrayList<Ztree>();
		for (PortaletRes res : resList) {
			Ztree ztree = new Ztree();
			ztree.setId(res.getId());
			ztree.setpId(res.getParentId());
			ztree.setKey(String.valueOf(res.getId()) + "|" + res.getResType());
			ztree.setValue(res.getCnName());
			ztree.setName(res.getCnName());
			ztree.setTitle(res.getCnName());
			if(Boolean.TRUE ==  parentNoCheckFlag){
				ztree.setNocheck(true);
			}
			ztrees.add(ztree);
		}
		return ztrees;
	}

}
