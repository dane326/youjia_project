package com.gsh.system.queryport.domain;

import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 查询视图column对象 sys_queryport_column
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
public class QueryportOption extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 标题 */
    @Excel(name = "title")
    private String title;

    /** 图例数据 */
    @Excel(name = "图例数据")
    private String legendData;

    /** x轴数据 */
    @Excel(name = "x轴数据")
    private String xAxisData;

    /** y轴数据 */
    @Excel(name = "y轴数据")
    private String yAxisData;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLegendData() {
        return legendData;
    }

    public void setLegendData(String legendData) {
        this.legendData = legendData;
    }

    public String getxAxisData() {
        return xAxisData;
    }

    public void setxAxisData(String xAxisData) {
        this.xAxisData = xAxisData;
    }

    public String getyAxisData() {
        return yAxisData;
    }

    public void setyAxisData(String yAxisData) {
        this.yAxisData = yAxisData;
    }

    @Override
    public String toString() {
        return "QueryportOption{" +
                "title='" + title + '\'' +
                ", legendData='" + legendData + '\'' +
                ", xAxisData='" + xAxisData + '\'' +
                ", yAxisData='" + yAxisData + '\'' +
                '}';
    }
}
