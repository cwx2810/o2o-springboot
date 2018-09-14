package com.imooc.o2o.service;

import com.imooc.o2o.entity.HeadLine;

import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-12 14:54
 **/
public interface HeadLineService {

    /**
     * 获取头条列表
     * @param headLineCondition
     * @return
     * @throws Exception
     */
    List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws Exception;
}
