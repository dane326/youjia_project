package com.gsh.system.service.impl;

import com.gsh.common.config.Global;
import com.gsh.common.core.text.Convert;
import com.gsh.common.exception.file.FileNameLengthLimitExceededException;
import com.gsh.common.utils.DateUtils;
import com.gsh.common.utils.file.FileUploadUtils;
import com.gsh.common.utils.file.MimeTypeUtils;
import com.gsh.common.utils.file.MinioClientUtils;
import com.gsh.system.domain.SysAccessories;
import com.gsh.system.mapper.SysAccessoriesMapper;
import com.gsh.system.service.ISysAccessoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 附件Service业务层处理
 * 
 * @author gsh
 * @date 2019-09-24
 */
@Service
public class SysAccessoriesServiceImpl implements ISysAccessoriesService 
{
    @Autowired
    private SysAccessoriesMapper sysAccessoriesMapper;

    /**
     * 上传附件
     *
     * @param multipartFile 附件
     * @return 附件路径
     */
    @Override
    public String uploadSysAccessories(MultipartFile multipartFile) throws Exception {
        if (multipartFile.getOriginalFilename().length() > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }
        FileUploadUtils.assertAllowed(multipartFile, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        if(Global.isMinioFileStorageMode()) {
            String fileName = MinioClientUtils.getInstance().uploadFile("iexam", multipartFile);
            return fileName;
        }else {
            String fileName = FileUploadUtils.extractFilename(multipartFile);
            File desc = FileUploadUtils.getAbsoluteFile(FileUploadUtils.getDefaultBaseDir(), fileName);
            multipartFile.transferTo(desc);
            return desc.getAbsolutePath();
        }
    }

    /**
     * 查询附件
     * 
     * @param id 附件ID
     * @return 附件
     */
    @Override
    public SysAccessories selectSysAccessoriesById(Long id)
    {
        return sysAccessoriesMapper.selectSysAccessoriesById(id);
    }

    /**
     * 查询附件列表
     * 
     * @param sysAccessories 附件
     * @return 附件
     */
    @Override
    public List<SysAccessories> selectSysAccessoriesList(SysAccessories sysAccessories)
    {
        return sysAccessoriesMapper.selectSysAccessoriesList(sysAccessories);
    }

    /**
     * 新增附件
     *
     * @param sysAccessories 附件
     * @return 结果
     */
    @Override
    public SysAccessories insertSysAccessories(MultipartFile multipartFile, String[] allowedExtension) throws Exception {
        FileUploadUtils.assertAllowed(multipartFile, allowedExtension);
        String filePathName = uploadSysAccessories(multipartFile);
        SysAccessories sysAccessories = new SysAccessories();
        sysAccessories.setDeleted("0");//查询未删除的数据
        sysAccessories.setFileName(multipartFile.getOriginalFilename());
        sysAccessories.setPath(filePathName);
        insertSysAccessories(sysAccessories);
        return sysAccessories;
    }

    /**
     * 新增附件
     * 
     * @param sysAccessories 附件
     * @return 结果
     */
    @Override
    public int insertSysAccessories(SysAccessories sysAccessories)
    {
        sysAccessories.setCreateTime(DateUtils.getNowDate());
        return sysAccessoriesMapper.insertSysAccessories(sysAccessories);
    }

    /**
     * 修改附件
     * 
     * @param sysAccessories 附件
     * @return 结果
     */
    @Override
    public int updateSysAccessories(SysAccessories sysAccessories)
    {
        sysAccessories.setUpdateTime(DateUtils.getNowDate());
        return sysAccessoriesMapper.updateSysAccessories(sysAccessories);
    }

    /**
     * 删除附件对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysAccessoriesByIds(String ids)
    {
        return sysAccessoriesMapper.deleteSysAccessoriesByIds(Convert.toStrArray(ids));
    }

    /**
     * 批量查询附件
     *
     * @param ids 需要查询的数据ID
     * @return 结果
     */
    @Override
    public List<SysAccessories> selectSysAccessoriesByIds(String ids){
        return sysAccessoriesMapper.selectSysAccessoriesByIds(Convert.toStrArray(ids));
    }
    /**
     * 删除附件信息
     * 
     * @param id 附件ID
     * @return 结果
     */
    public int deleteSysAccessoriesById(Long id)
    {
        return sysAccessoriesMapper.deleteSysAccessoriesById(id);
    }

    /**
     * 逻辑删除附件对象
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int logicDeleteSysAccessoriesByIds(String ids)
    {
        return sysAccessoriesMapper.logicDeleteSysAccessoriesByIds(Convert.toStrArray(ids));
    }

    /**
     * 逻辑删除附件信息
     *
     * @param id 附件ID
     * @return 结果
     */
    public int logicDeleteSysAccessoriesById(Long id)
    {
        return sysAccessoriesMapper.logicDeleteSysAccessoriesById(id);
    }

}
