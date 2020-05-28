package com.gsh.system.portalet.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsh.common.core.text.Convert;
import com.gsh.common.utils.DateUtils;
import com.gsh.system.portalet.domain.PortaletPage;
import com.gsh.system.portalet.domain.PortaletRes;
import com.gsh.system.portalet.mapper.PortaletPageMapper;
import com.gsh.system.portalet.mapper.PortaletResMapper;
import com.gsh.system.portalet.service.IPortaletPageService;

/**
 * 自定义portal页面Service业务层处理
 * 
 * @author gsh
 * @date 2020-03-17
 */
@Service
public class PortaletPageServiceImpl implements IPortaletPageService {
	@Autowired
	private PortaletPageMapper portaletPageMapper;

	@Autowired
	private PortaletResMapper portaletResMapper;

	/**
	 * 查询自定义portal页面
	 * 
	 * @param id
	 *            自定义portal页面ID
	 * @return 自定义portal页面
	 */
	@Override
	public PortaletPage selectPortaletPageById(Long id) {
		return portaletPageMapper.selectPortaletPageById(id);
	}

	/**
	 * 查询自定义portal页面列表
	 * 
	 * @param portaletPage
	 *            自定义portal页面
	 * @return 自定义portal页面
	 */
	@Override
	public List<PortaletPage> selectPortaletPageList(PortaletPage portaletPage) {
		return portaletPageMapper.selectPortaletPageList(portaletPage);
	}

	/**
	 * 新增自定义portal页面
	 * 
	 * @param portaletPage
	 *            自定义portal页面
	 * @return 结果
	 */
	@Override
	public int insertPortaletPage(PortaletPage portaletPage) {
		portaletPage.setCreateTime(DateUtils.getNowDate());
		return portaletPageMapper.insertPortaletPage(portaletPage);
	}

	/**
	 * 修改自定义portal页面
	 * 
	 * @param portaletPage
	 *            自定义portal页面
	 * @return 结果
	 */
	@Override
	public int updatePortaletPage(PortaletPage portaletPage) {
		portaletPage.setUpdateTime(DateUtils.getNowDate());
		
		if ("sys".equals(portaletPage.getResType())) {
			PortaletPage curPage = portaletPageMapper.selectPortaletPageById(portaletPage.getId());
			if (!curPage.getSystemCode().equals(portaletPage.getSystemCode())
					|| curPage.getSystemName().equals(portaletPage.getSystemName())) {
				PortaletPage conPage = new PortaletPage();
				conPage.setSystemCode(curPage.getSystemCode());
				// 更新所有子节点				
				List<PortaletPage> pages = portaletPageMapper.selectPortaletPageList(conPage);
				for (PortaletPage page : pages) {
					if(!page.getId().equals(portaletPage.getId())){
						page.setSystemName(portaletPage.getSystemName());
						page.setSystemCode(portaletPage.getSystemCode());
						portaletPageMapper.updatePortaletPage(page);
					}					
				}
			}
		} else if ("module".equals(portaletPage.getResType())) {
			PortaletPage curPage = portaletPageMapper.selectPortaletPageById(portaletPage.getId());
			if (!curPage.getModuleCode().equals(portaletPage.getModuleCode())
					|| curPage.getModuleName().equals(portaletPage.getModuleName())) {
				// 更新所有的子节点
				PortaletPage conPage = new PortaletPage();
				conPage.setParentId(portaletPage.getId());
				// 更新所有子节点
				List<PortaletPage> pages = portaletPageMapper.selectPortaletPageList(conPage);
				for (PortaletPage page : pages) {
					page.setModuleName(portaletPage.getModuleName());
					page.setModuleCode(portaletPage.getModuleCode());
					portaletPageMapper.updatePortaletPage(page);
				}
			}
		}
		
		return portaletPageMapper.updatePortaletPage(portaletPage);
	}

	/**
	 * 修改自定义portal页面
	 * 
	 * @param portaletPages
	 *            自定义portal页面
	 * @return 结果
	 */
	@Override
	public int updatePortaletPages(List<PortaletPage> portaletPages) {
		if(CollectionUtils.isNotEmpty(portaletPages)){
			for(PortaletPage portaletPage : portaletPages){
				portaletPage.setUpdateTime(DateUtils.getNowDate());
				portaletPageMapper.updatePortaletPage(portaletPage);
			}
			
			return 1;
		}
		
		return 0;
	}

	/**
	 * 删除自定义portal页面对象
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deletePortaletPageByIds(String ids) {
		return portaletPageMapper.deletePortaletPageByIds(Convert.toStrArray(ids));
	}

	/**
	 * 删除自定义portal页面信息
	 * 
	 * @param id
	 *            自定义portal页面ID
	 * @return 结果
	 */
	@Override
	public int deletePortaletPageById(Long id) {
		return portaletPageMapper.deletePortaletPageById(id);
	}

	/**
	 * 逻辑删除自定义portal页面对象
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int logicDeletePortaletPageByIds(String ids) {
		return portaletPageMapper.logicDeletePortaletPageByIds(Convert.toStrArray(ids));
	}

	/**
	 * 逻辑删除自定义portal页面信息
	 *
	 * @param id
	 *            自定义portal页面ID
	 * @return 结果
	 */
	public int logicDeletePortaletPageById(Long id) {
		return portaletPageMapper.logicDeletePortaletPageById(id);
	}

	/**
	 * 增加资源
	 * 
	 * @param modulePageId
	 * @param ids
	 * @return
	 */
	@Override
	public int insertResByIds(Long modulePageId, String ids) {
		if (StringUtils.isNotBlank(ids)) {
			PortaletPage modulePage = portaletPageMapper.selectPortaletPageById(modulePageId);
			// 获取资源最大sortNo
			Long sortNo = this.selectMaxSortNoByParentId(modulePageId);
			sortNo = (sortNo == null ? 0 : sortNo) + 1;
			String[] idList = StringUtils.split(ids, ",");
			for (int ix = 0; ix < idList.length; ix++) {
				String id = idList[ix];
				PortaletRes res = portaletResMapper.selectPortaletResById(Long.valueOf(id));
				PortaletPage portaletPage = new PortaletPage();
				portaletPage.setParentId(modulePageId);
				portaletPage.setSystemCode(modulePage.getSystemCode());
				portaletPage.setSystemName(modulePage.getSystemName());
				portaletPage.setModuleCode(modulePage.getModuleCode());
				portaletPage.setModuleName(modulePage.getModuleName());
				portaletPage.setCnName(res.getResName());
				portaletPage.setResId(res.getId());
				portaletPage.setResType(res.getResType());
				portaletPage.setWidth(res.getWidth());
				portaletPage.setHeight(res.getHeight());
				portaletPage.setDeleted(res.getDeleted());
				portaletPage.setSortNo(sortNo + ix);
				portaletPageMapper.insertPortaletPage(portaletPage);
			}
		}

		return 1;
	}

	/**
	 * 获取模块下的最大排序号
	 * 
	 * @param parentId
	 * @return
	 */
	@Override
	public Long selectMaxSortNoByParentId(Long parentId) {
		return portaletPageMapper.selectMaxSortNoByParentId(parentId);
	}
}
