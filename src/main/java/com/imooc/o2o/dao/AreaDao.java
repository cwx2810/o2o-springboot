package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Area;

import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-02 18:12
 **/
public interface AreaDao {
    /**
     * 查询区域
     * @return areaList
     */
    List<Area> queryArea();
}
