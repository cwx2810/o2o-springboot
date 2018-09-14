package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.HeadLineDao;
import com.imooc.o2o.entity.HeadLine;
import com.imooc.o2o.service.HeadLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-12 14:56
 **/
@Service
public class HeadLineServiceImpl implements HeadLineService {

    @Autowired
    private HeadLineDao headLineDao;

    /**
     * 获取头条列表
     * @param headLineCondition
     * @return
     * @throws Exception
     */
    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws Exception {
        return headLineDao.queryHeadLine(headLineCondition);
    }
}
