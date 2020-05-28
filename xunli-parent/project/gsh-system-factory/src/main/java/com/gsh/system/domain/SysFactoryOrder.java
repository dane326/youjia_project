package com.gsh.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 订单管理对象 sys_factory_order
 * 
 * @author gsh
 * @date 2019-10-30
 */
public class SysFactoryOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNo;

    /** 权益类型 */
    @Excel(name = "权益类型")
    private String orderType;

    /** 权益值 */
    @Excel(name = "权益值")
    private String orderValue;

    /** 购买类型 */
    @Excel(name = "购买类型")
    private String buyType;

    /** 购买方式 */
    @Excel(name = "购买方式")
    private String buyMethod;

    /** 购买数量 */
    @Excel(name = "购买数量")
    private String buyNum;

    /** 购买期限 */
    @Excel(name = "购买期限", width = 30, dateFormat = "yyyy-MM-dd")
    private Date buyPeriod;

    /** 订单名称 */
    @Excel(name = "订单名称")
    private String orderSubject;

    /** 付款额 */
    @Excel(name = "付款额")
    private String money;

    /** 已开发票 */
    @Excel(name = "已开发票")
    private String invoiced;

    /** 付款状态 */
    @Excel(name = "付款状态")
    private String payStatus;
    
    /** 付款方式 */
    @Excel(name = "付款方式")
    private String payMethod;

    /** 交易凭证 */
    @Excel(name = "交易凭证")
    private String tradeNo;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }
    public void setOrderType(String orderType) 
    {
        this.orderType = orderType;
    }

    public String getOrderType() 
    {
        return orderType;
    }
    
    public String getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(String orderValue) {
		this.orderValue = orderValue;
	}

	public void setBuyType(String buyType) 
    {
        this.buyType = buyType;
    }

    public String getBuyType() 
    {
        return buyType;
    }
    public void setBuyMethod(String buyMethod) 
    {
        this.buyMethod = buyMethod;
    }

    public String getBuyMethod() 
    {
        return buyMethod;
    }
    public void setBuyNum(String buyNum) 
    {
        this.buyNum = buyNum;
    }

    public String getBuyNum() 
    {
        return buyNum;
    }
    public void setBuyPeriod(Date buyPeriod) 
    {
        this.buyPeriod = buyPeriod;
    }

    public Date getBuyPeriod() 
    {
        return buyPeriod;
    }
    public void setOrderSubject(String orderSubject) 
    {
        this.orderSubject = orderSubject;
    }

    public String getOrderSubject() 
    {
        return orderSubject;
    }
    public void setMoney(String money) 
    {
        this.money = money;
    }

    public String getMoney() 
    {
        return money;
    }
    public void setInvoiced(String invoiced) 
    {
        this.invoiced = invoiced;
    }

    public String getInvoiced() 
    {
        return invoiced;
    }
    public void setPayStatus(String payStatus) 
    {
        this.payStatus = payStatus;
    }

    public String getPayStatus() 
    {
        return payStatus;
    }
    public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public void setTradeNo(String tradeNo) 
    {
        this.tradeNo = tradeNo;
    }

    public String getTradeNo() 
    {
        return tradeNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("orderType", getOrderType())
            .append("orderValue", getOrderValue())
            .append("buyType", getBuyType())
            .append("buyMethod", getBuyMethod())
            .append("buyNum", getBuyNum())
            .append("buyPeriod", getBuyPeriod())
            .append("orderSubject", getOrderSubject())
            .append("money", getMoney())
            .append("invoiced", getInvoiced())
            .append("payStatus", getPayStatus())
            .append("payMethod", getPayMethod())
            .append("tradeNo", getTradeNo())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("deleted", getDeleted())
            .toString();
    }
}