package com.imooc.o2o.util;

/**
 * @author: LieutenantChen
 * @create: 2018-09-07 14:22
 **/
public class PageCalculator {
    /**
     * 通过页码和每页店铺条数，计算出每页第一行，是数据库表中的第几条
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static int calculatorRowIndex(int pageIndex, int pageSize) {
        return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
    }
}
