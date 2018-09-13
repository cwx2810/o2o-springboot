package com.imooc.o2o.dao;

import com.imooc.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-12 14:40
 **/
public interface HeadLineDao {

    /**
     * 根据查询条件查询头条列表
     * @param headLineCondition
     * @return
     */
    List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);
}
