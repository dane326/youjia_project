package com.gsh.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gsh.common.annotation.DataScope;
import com.gsh.common.config.Global;
import com.gsh.common.constant.UserConstants;
import com.gsh.common.core.text.Convert;
import com.gsh.common.exception.BusinessException;
import com.gsh.common.utils.DateUtils;
import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.security.Md5Utils;
import com.gsh.system.domain.SysDept;
import com.gsh.system.domain.SysPost;
import com.gsh.system.domain.SysRole;
import com.gsh.system.domain.SysUser;
import com.gsh.system.domain.SysUserLabel;
import com.gsh.system.domain.SysUserPost;
import com.gsh.system.domain.SysUserRole;
import com.gsh.system.mapper.SysDeptMapper;
import com.gsh.system.mapper.SysPostMapper;
import com.gsh.system.mapper.SysRoleMapper;
import com.gsh.system.mapper.SysUserLabelMapper;
import com.gsh.system.mapper.SysUserMapper;
import com.gsh.system.mapper.SysUserPostMapper;
import com.gsh.system.mapper.SysUserRoleMapper;
import com.gsh.system.service.ISysConfigService;
import com.gsh.system.service.ISysUserService;
import com.gsh.system.service.ISysVipService;
import com.gsh.system.utils.FactoryUtils;

/**
 * 用户 业务层处理
 * 
 * @author gsh
 */
@Service
public class SysUserServiceImpl implements ISysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserLabelMapper sysUserLabelMapper;

    @Autowired
    private ISysConfigService configService;
    
    @Autowired
    private SysDeptMapper deptMapper;
    
    @Autowired(required = false)
    private ISysVipService sysVipService;

    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")

    public List<SysUser> selectUserList(SysUser user)
    {
        return userMapper.selectUserList(user);
    }

    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList()
    {
        return userMapper.selectUserList(new SysUser());
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user)
    {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user)
    {
        return userMapper.selectUnallocatedList(user);
    }
    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByLoginName(String userName)
    {
        return userMapper.selectUserByLoginName(userName);
    }

    /**
     * 通过手机号码查询用户
     * 
     * @param phoneNumber 手机号码
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByPhoneNumber(String phoneNumber)
    {
        return userMapper.selectUserByPhoneNumber(phoneNumber);
    }

    /**
     * 通过邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByEmail(String email)
    {
        return userMapper.selectUserByEmail(email);
    }

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId)
    {
        return userMapper.selectUserById(userId);
    }

    /**
     * 通过用户ID查询用户和角色关联
     * 
     * @param userId 用户ID
     * @return 用户和角色关联列表
     */
    public List<SysUserRole> selectUserRoleByUserId(Long userId)
    {
        return userRoleMapper.selectUserRoleByUserId(userId);
    }

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteUserById(Long userId)
    {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(String ids) throws BusinessException
    {
        Long[] userIds = Convert.toLongArray(ids);
        for (Long userId : userIds)
        {
            checkUserAllowed(new SysUser(userId));
        }
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 新增保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user)
    {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user.getUserId(), user.getRoleIds());
        return rows;
    }

    /**
     * 注册用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public boolean registerUser(SysUser user)
    {
        //user.setUserType(UserConstants.REGISTER_USER_TYPE);
        return userMapper.insertUser(user) > 0;
    }

    /**
     * 修改保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user)
    {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user.getUserId(), user.getRoleIds());
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户个人详细信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserInfo(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 用户授权角色
     * 
     * @param userId 用户ID
     * @param roleIds 角色组
     */
    public void insertUserAuth(Long userId, Long[] roleIds)
    {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetUserPwd(SysUser user)
    {
        return updateUserInfo(user);
    }

    /**
     * 新增用户角色信息
     * 
     */
    public void insertUserRole(Long userId, Long[] roleIds)
    {
        if (StringUtils.isNotNull(roleIds))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roleIds)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     * 
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user)
    {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts))
        {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts)
            {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0)
            {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 校验登录名称是否唯一
     * 
     * @param loginName 用户名
     * @return
     */
    @Override
    public String checkLoginNameUnique(String loginName)
    {
        int count = userMapper.checkLoginNameUnique(loginName);
        if (count > 0)
        {
            return UserConstants.USER_NAME_NOT_UNIQUE;
        }
        return UserConstants.USER_NAME_UNIQUE;
    }

    /**
     * 校验登录名称是否唯一
     *
     * @param loginName 用户名
     * @return
     */
    @Override
    public String checkLoginNameUnique(String loginName, Long factoryCode)
    {
        int count = userMapper.checkLoginNameUniquetwo(loginName, factoryCode);
        if (count > 0)
        {
            return UserConstants.USER_NAME_NOT_UNIQUE;
        }
        return UserConstants.USER_NAME_UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.USER_PHONE_NOT_UNIQUE;
        }
        return UserConstants.USER_PHONE_UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.USER_EMAIL_NOT_UNIQUE;
        }
        return UserConstants.USER_EMAIL_UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    public void checkUserAllowed(SysUser user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
        {
            throw new BusinessException("不允许操作超级管理员用户");
        }
    }

    /**
     * 查询用户所属角色组
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(Long userId)
    {
        List<SysRole> list = roleMapper.selectRolesByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list)
        {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(Long userId)
    {
        List<SysPost> list = postMapper.selectPostsByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list)
        {
            if(post != null){
                idsStr.append(post.getPostName()).append(",");
            }
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 导入用户数据
     * 
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new BusinessException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList)
        {
            String checkResult=checkUserImport(user);
            if(!"".equals(checkResult)){
                failureNum++;
                failureMsg.append("<br/>"+failureNum+"、第"+(failureNum+1)+"行导入失败!"+checkResult+"都不能为空！");
                continue;
            }
            try
            {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByLoginName(user.getLoginName());

                if (StringUtils.isNull(u))
                {
                    user.setPassword(Md5Utils.hash(user.getLoginName() + password));
                    user.setCreateBy(operName);
                    user.setCreateTime(new Date());
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getLoginName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getLoginName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 帐号 用户名 部门 电话都不能为空 导入才有效
     * @param user
     * @return
     */
    private String checkUserImport(SysUser user) {
        String checkResult="";
         if(user.getLoginName()==null||"".equals(user.getLoginName())){
             checkResult+="帐号；";
         }
        if(user.getUserName()==null||"".equals(user.getUserName())){
            checkResult+="用户名；";
        }
        if(user.getDeptId()==null||"".equals(user.getDeptId())){
            checkResult+="部门编号；";
        }
        if(user.getPhonenumber()==null||"".equals(user.getPhonenumber())){
            checkResult+="电话号码；";
        }
        return checkResult;
    }

    /**
     * 用户状态修改
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int changeStatus(SysUser user)
    {
        //todo 貌似ry版本中更新漏掉了这里的校验 add by  wyk  191212
        checkUserAllowed(new SysUser(user.getUserId()));

        return userMapper.updateUser(user);
    }

    /**
     * 查询人员信息树
     *
     * @param id 部门id
     * @param searchKey
     * @return 所有人员信息
     */
    @Override
    public Object selectDeptUserTree(Boolean root, Long id, String searchKey) {
        if(id == null){
            id = 0L;
        }
        SysDept sysDept = new SysDept();
        if(StringUtils.isNotEmpty(searchKey)){
            sysDept.setDeptName(searchKey);
        }else{
            sysDept.setParentId(id);
        }
        JSONArray array = new JSONArray();
        List<SysDept> deptList = deptMapper.selectDeptList(sysDept);
        if(root!=null && root && id > 0){
            SysDept firstDept = deptMapper.selectDeptById(id);
            if(firstDept != null){
                deptList.add(firstDept);
            }
        }
        for (SysDept dept : deptList) {
            if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
                JSONObject json = getZTreeNodeObject(String.valueOf(dept.getDeptId()), dept.getDeptName(),String.valueOf(dept.getParentId()), dept.getDeptName());
                json.put("key", dept.getDeptId());
                json.put("type", "dept");
                json.put("value", dept.getDeptName());
                json.put("isParent", "true");
                array.add(json);
            }
        }

        SysUser sysUser = new SysUser();
        if(StringUtils.isNotEmpty(searchKey)){
            sysUser.setUserName(searchKey);
        }else{
            sysUser.setDeptId(id);
        }
        List<SysUser> userList = userMapper.selectUserTreeList(sysUser);
        for (SysUser user : userList) {
            if (UserConstants.NORMAL.equals(user.getStatus())) {
                JSONObject json = getZTreeNodeObject(String.valueOf(user.getUserId()), user.getUserName(), String.valueOf(user.getDeptId()), String.format("%s[%s]", user.getUserName(), user.getLoginName()));
                json.put("key", user.getLoginName());
                json.put("type", "user");
                json.put("value", user.getUserName());
                array.add(json);
            }
        }
        return array;
    }

    private JSONObject getZTreeNodeObject(String id, String name ,String pId, String title) {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("pId", pId);
        json.put("title", title);
        return json;
    }

    /**
     * 查询成员
     *
     * @param sysUser 成员ID
     * @return 成员
     */
    @Override
    public SysUser selectSysUserByProperties(SysUser sysUser){
        return userMapper.selectSysUserByProperties(sysUser);
    }

    /**
     * 修改成员
     *
     * @return 结果
     */
    @Override
    @Transactional
    public int updateSysFactoryUser(SysUser sysUser,String lableIds)
    {
        sysUser.setUpdateTime(DateUtils.getNowDate());
        int rows =userMapper.updateUser(sysUser);
        //添加标签信息
        sysUserLabelMapper.deleteSysUserLabelById(sysUser.getUserId());
        insertUserLable(sysUser,lableIds);
        return rows;
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserLable(SysUser user,String lableIds)
    {

        String[] lables = lableIds.split(",");
        if (StringUtils.isNotNull(lables))
        {
            // 新增用户与岗位管理
            List<SysUserLabel> list = new ArrayList<SysUserLabel>();
            for (String lableId : lables)
            {
                SysUserLabel up = new SysUserLabel();
                up.setUserId(user.getUserId());
                if("".equals(lableId))continue;
                up.setLabelId(Long.parseLong(lableId));
                list.add(up);
            }
            if (list.size() > 0)
            {
                sysUserLabelMapper.batchSysFactoryUserLabel(list);
            }
        }
    }
    /**
     * 批量更新字段
     * @param userIds
     * @param sysFactoryUser
     * @return
     */
    public int updateSysUsersByIds(String userIds,  SysUser sysFactoryUser){
        for(String id:userIds.split(",")){
            userRoleMapper.deleteUserRoleByUserId(Long.parseLong(id));
        }
        return userMapper.updateSysUsersByIds(Convert.toStrArray(userIds),sysFactoryUser);
    }

    /**
     * 新增成员
     *
     * @param sysFactoryUser 成员
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSysFactoryUser(SysUser sysFactoryUser,String lableIds)
    {
    	if(sysFactoryUser.getFactoryCode() == null){
    		sysFactoryUser.setFactoryCode(FactoryUtils.getFactoryCode());
    	}
    	
    	// 判断租户的成员账号数是否超限
    	if(sysVipService != null){
    		sysVipService.checkVipAccountsNum(Global.getContextPathNoSeparator(), sysFactoryUser.getFactoryCode());
    	}
    	
    	//无须此处设置系统编码,拦截器统一处理
        sysFactoryUser.setDelFlag("0");//查询未删除的数据
        sysFactoryUser.setStatus(UserConstants.NORMAL);
        if(StringUtils.isEmpty(sysFactoryUser.getPassword())){
            String password = configService.selectConfigByKey("sys.factory.user.initPassword");
            sysFactoryUser.setPassword(password);
        }
        sysFactoryUser.setSalt(FactoryUtils.randomSalt());
        sysFactoryUser.setPassword(FactoryUtils.encryptPassword(sysFactoryUser.getLoginName(), sysFactoryUser.getPassword(), sysFactoryUser.getSalt()));
        sysFactoryUser.setCreateTime(DateUtils.getNowDate());
        // 新增用户信息
        int rows = userMapper.insertSysFactoryUser(sysFactoryUser);
        // 新增用户标签关联
        insertUserLable(sysFactoryUser,lableIds);
        //新增用户角色 wyk 2020-0325 角色成员管理人自己分配
//        List<SysUserRole> userRoleList = new ArrayList<SysUserRole>();
//        SysUserRole sysUserRole= new SysUserRole();
//        sysUserRole.setUserId(sysFactoryUser.getUserId());
//        String roleId = configService.selectConfigByKey("sys:factory:studentRoleId");
//        if(!"".equals(roleId)){
//           sysUserRole.setRoleId(Long.parseLong(roleId));
//        }
//        userRoleList.add(sysUserRole);
//        sysUserRoleMapper.batchUserRole(userRoleList);
        return rows;
    }

    /**
     * 批量更新字段
     * @param userIds
     * @param sysFactoryUser
     * @return
     */
    public int updateSysFactoryUsersByIds(String userIds,  SysUser sysFactoryUser){
        for(String id:userIds.split(",")){
            userRoleMapper.deleteUserRoleByUserId(Long.parseLong(id));
        }
        return userMapper.updateSysUsersByIds(Convert.toStrArray(userIds),sysFactoryUser);
    }

    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectSysUserListAndPaper(SysUser sysUser) {
        return userMapper.selectSysUserListAndPaper(sysUser);
    }

    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectSysUserListByDeptAndPaper(Long deptid, Long paperid) {
        return userMapper.selectSysUserListByDeptAndPaper(deptid,paperid);
    }

    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectSysUserListByLabelAndPaper(String labelIds, Long paperid) {
        return userMapper.selectSysUserListByLabelAndPaper(Convert.toStrArray(labelIds),paperid);
    }
}
