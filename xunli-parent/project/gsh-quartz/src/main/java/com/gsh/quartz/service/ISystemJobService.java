package com.gsh.quartz.service;

/**
 * Created by Administrator on 2020/1/9 0009.
 */
public interface ISystemJobService {
    /**
     * 清理指定表的历史数据
     * @param tbName     表名
     * @param num        数量：比如多少年，多少月，多少天，多少小时，多少分钟，多少秒
     * @param type       单位：YEAR，MONTH，DAY，HOUR，MINUTE，SECOND
     */
    public  void  clearSystemTableData(String tbName, String num, String type);
}
