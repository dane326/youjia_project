package com.gsh.system.queryport.domain;

import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;

/**
 * 查询视图column对象 sys_queryport_chart
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
public class QueryportChart extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** x轴 */
    @Excel(name = "x轴")
    private String xaxis;

    /** y轴 */
    @Excel(name = "y轴")
    private String yaxis;

    /** 形状名称 */
    @Excel(name = "形状名称")
    private String shapeType;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 图例 */
    @Excel(name = "图例")
    private String legend;

    /** 主表id */
    @Excel(name = "主表id")
    private Long mainId;

    /** 图形属性 */
    @Excel(name = "图形属性")
    private String option;

    /** 顺序 */
    @Excel(name = "顺序")
    private String displayOrder;


    /** 删除标记 */
    @Excel(name = "删除标记")
    private String deleted;

    /** 高度 */
    @Excel(name = "高度")
    private String height;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getXaxis() {
        return xaxis;
    }

    public void setXaxis(String xaxis) {
        this.xaxis = xaxis;
    }

    public String getYaxis() {
        return yaxis;
    }

    public void setYaxis(String yaxis) {
        this.yaxis = yaxis;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public Long getMainId() {
        return mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public String getDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }



    @Override
    public String toString() {
        return "QueryportChart{" +
                "id=" + id +
                ", xaxis='" + xaxis + '\'' +
                ", yaxis='" + yaxis + '\'' +
                ", shapeType='" + shapeType + '\'' +
                ", title='" + title + '\'' +
                ", legend='" + legend + '\'' +
                ", mainId='" + mainId + '\'' +
                ", option='" + option + '\'' +
                ", displayOrder='" + displayOrder + '\'' +
                ", deleted='" + deleted + '\'' +
                ", height='" + height + '\'' +
                '}';
    }
}
