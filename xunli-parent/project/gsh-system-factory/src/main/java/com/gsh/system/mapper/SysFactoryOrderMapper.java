package com.gsh.system.mapper;

import com.gsh.system.domain.SysFactoryOrder;
import java.util.List;

/**
 * 订单管理Mapper接口
 * 
 * @author gsh
 * @date 2019-10-29
 */
public interface SysFactoryOrderMapper 
{
    /**
     * 查询订单管理
     * 
     * @param id 订单管理ID
     * @return 订单管理
     */
    public SysFactoryOrder selectSysFactoryOrderById(Long id);

    /**
     * 查询订单管理列表
     * 
     * @param sysFactoryOrder 订单管理
     * @return 订单管理集合
     */
    public List<SysFactoryOrder> selectSysFactoryOrderList(SysFactoryOrder sysFactoryOrder);

    /**
     * 新增订单管理
     * 
     * @param sysFactoryOrder 订单管理
     * @return 结果
     */
    public int insertSysFactoryOrder(SysFactoryOrder sysFactoryOrder);

    /**
     * 修改订单管理
     * 
     * @param sysFactoryOrder 订单管理
     * @return 结果
     */
    public int updateSysFactoryOrder(SysFactoryOrder sysFactoryOrder);

    /**
     * 删除订单管理
     *
     * @param id 订单管理ID
     * @return 结果
     */
    public int deleteSysFactoryOrderById(Long id);

    /**
     * 批量删除订单管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFactoryOrderByIds(String[] ids);

    /**
     *逻辑删除订单管理
     *
     * @param id 订单管理ID
     * @return 结果
     */
    public int logicDeleteSysFactoryOrderById(Long id);

    /**
     * 批量逻辑删除订单管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteSysFactoryOrderByIds(String[] ids);
}
