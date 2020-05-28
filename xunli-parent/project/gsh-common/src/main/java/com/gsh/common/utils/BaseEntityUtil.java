package com.gsh.common.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.gsh.common.config.FactoryConfig;
import com.gsh.common.config.Global;
import com.gsh.common.core.domain.BaseEntity;
import com.gsh.common.utils.bean.BeanUtils;

/**
 * cyq Created by Administrator on 2019/8/29 0029.
 */
public class BaseEntityUtil {
	private static final Logger log = LoggerFactory.getLogger(BaseEntityUtil.class);

	/**
	 * 这里只是为了获取人员id和名字，否则在pom引入gsh-system 会循环引包，无法编译
	 */
	private static class SysUser {
		String loginName;
		String userName;
		Long factoryCode;
		SysDept dept;

		public String getLoginName() {
			return loginName;
		}

		public void setLoginName(String loginName) {
			this.loginName = loginName;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public SysDept getDept() {
			return dept;
		}

		public void setDept(SysDept dept) {
			this.dept = dept;
		}

		public Long getFactoryCode() {
			return factoryCode;
		}

		public void setFactoryCode(Long factoryCode) {
			this.factoryCode = factoryCode;
		}
	}

	private static class SysDept {
		Long deptId;
		String deptName;
		Long factoryCode;

		public Long getDeptId() {
			return deptId;
		}

		public void setDeptId(Long deptId) {
			this.deptId = deptId;
		}

		public String getDeptName() {
			return deptName;
		}

		public void setDeptName(String deptName) {
			this.deptName = deptName;
		}
		
		public Long getFactoryCode() {
			return factoryCode;
		}

		public void setFactoryCode(Long factoryCode) {
			this.factoryCode = factoryCode;
		}
	}


	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	public static SysUser getSysUser() {
		try {
			Object obj = null;
			try {
				obj = getSubject().getPrincipal();
			} catch (Exception e) {
				return null;// 刚登录还没有人员信息的时候获取不到
			}

			if (StringUtils.isNull(obj)) {
				return null;
			}
			SysUser user = new SysUser();
			BeanUtils.copyBeanProp(user, obj);// 无法深度复制

			JSONObject jsonObj = (JSONObject) JSONObject.toJSON(obj);
			SysDept dept = JSONObject.parseObject(jsonObj.get("dept").toString(), SysDept.class);// json 转对象
			if (dept == null) {
				dept = new SysDept();
			}
			user.setDept(dept);

			return user;
		} catch (Exception e) {
			log.info("获取人员信息错误", e);
			return null;
		}
	}

	/**
	 * 添加数据时设置添加人信息
	 * 
	 * @param obj
	 */
	public static void setSaveInfo(BaseEntity obj) {
		if (null == obj) {
			return;
		}
		SysUser user = getSysUser();
		if (null == user) {
			return;
		}
		obj.setCreateBy(user.getLoginName() + "");
		obj.setCreateByName(user.getUserName());
		obj.setCreateTime(DateUtils.getNowDate());
		obj.setCreateByDeptid(user.getDept().getDeptId());
		obj.setCreateByDeptname(user.getDept().getDeptName());
		if(StringUtils.isEmpty(obj.getDeleted())){
			obj.setDeleted("0");
		}
		if(obj.getFactoryCode() == null){
			obj.setFactoryCode(getUserFactoryCode(user));
		}
		if(StringUtils.isBlank(obj.getContextName())){
			obj.setContextName(Global.getContextPathNoSeparator());
		}
	}

	/**
	 * 设置更新人信息
	 * 
	 * @param obj
	 */
	public static void setUpdateInfo(BaseEntity obj) {
		if (null == obj) {
			return;
		}
		SysUser user = getSysUser();
		if (null == user) {
			return;
		}
		obj.setUpdateBy(user.getLoginName() + "");
		obj.setUpdateByName(user.getUserName());
		obj.setUpdateTime(DateUtils.getNowDate());
		obj.setUpdateByDeptid(user.getDept().getDeptId());
		obj.setUpdateByDeptname(user.getDept().getDeptName());
		if(StringUtils.isEmpty(obj.getDeleted())){
			obj.setDeleted("0");
		}
		/*if(obj.getFactoryCode() == null){
			obj.setFactoryCode(getUserFactoryCode(user));
		}*/
		// 不更新factoryCode
		obj.setFactoryCode(null);
	}
	
	/**
	 * 设置查询信息
	 * 
	 * @param obj
	 */
	public static void setSelectInfo(BaseEntity obj) {
		if (null == obj) {
			return;
		}
		
		if(obj.getFactoryCode() == null){
			SysUser user = getSysUser();
			if (null == user) {
				return;
			}
			Long factoryCode = getUserFactoryCode(user);
			if(FactoryConfig.getPlatformFactoryCode().equals(factoryCode)){
				return;
			}
			obj.setFactoryCode(factoryCode);
		}
	}
	
	public static Long getUserFactoryCode(SysUser user){
		if(user.getFactoryCode() != null){
			return user.getFactoryCode();
		}
		return user.getDept().getFactoryCode();
	}
}
