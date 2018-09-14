package com.imooc.o2o.service;

import com.imooc.o2o.entity.ShopCategory;

import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-05 20:43
 **/
public interface ShopCategoryService {
    /**
     * 查询店铺分类
     * @param shopCategoryCondition
     * @return 店铺分类列表
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
