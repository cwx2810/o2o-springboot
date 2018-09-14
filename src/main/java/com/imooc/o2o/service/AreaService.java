package com.imooc.o2o.service;

import com.imooc.o2o.entity.Area;

import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-02 18:22
 **/
public interface AreaService {
    /**
     * 获得区域列表
     * @return areaList
     */
    List<Area> getAreaList();
}
