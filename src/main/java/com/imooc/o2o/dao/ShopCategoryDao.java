package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-05 19:23
 **/
public interface ShopCategoryDao {
    /**
     * 查询店铺分类
     * @param shopCategoryCondition 传入一个本类的参数，在sql里能写语句得到我们想要的parentId
     * @return 店铺分类列表
     */
    List<ShopCategory> queryShopCategory(
            @Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}
