package com.gsh.system.service;

import com.gsh.system.domain.SysAccessories;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 附件Service接口
 * 
 * @author gsh
 * @date 2019-09-24
 */
public interface ISysAccessoriesService 
{

    /**
     * 上传附件
     *
     * @param multipartFile 附件
     * @return 附件路径
     */
    public String uploadSysAccessories(MultipartFile multipartFile) throws Exception;

    /**
     * 查询附件
     * 
     * @param id 附件ID
     * @return 附件
     */
    public SysAccessories selectSysAccessoriesById(Long id);

    /**
     * 查询附件列表
     * 
     * @param sysAccessories 附件
     * @return 附件集合
     */
    public List<SysAccessories> selectSysAccessoriesList(SysAccessories sysAccessories);

    /**
     * 新增附件
     *
     * @param multipartFile 附件
     * @param allowedExtension 允许上传的类型
     * @return 结果
     */
    public SysAccessories insertSysAccessories(MultipartFile multipartFile, String[] allowedExtension) throws Exception;

    /**
     * 新增附件
     * 
     * @param sysAccessories 附件
     * @return 结果
     */
    public int insertSysAccessories(SysAccessories sysAccessories);

    /**
     * 修改附件
     * 
     * @param sysAccessories 附件
     * @return 结果
     */
    public int updateSysAccessories(SysAccessories sysAccessories);

    /**
     * 批量删除附件
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysAccessoriesByIds(String ids);


    /**
     * 批量查询附件
     *
     * @param ids 需要查询的数据ID
     * @return 结果
     */
    public List<SysAccessories> selectSysAccessoriesByIds(String ids);
    /**
     * 删除附件信息
     * 
     * @param id 附件ID
     * @return 结果
     */
    public int deleteSysAccessoriesById(Long id);
    /**
     * 批量逻辑删除附件
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteSysAccessoriesByIds(String ids);

    /**
     * 逻辑删除附件信息
     *
     * @param id 附件ID
     * @return 结果
     */
    public int logicDeleteSysAccessoriesById(Long id);

}
