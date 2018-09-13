package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-07 21:07
 **/
public interface ProductCategoryDao {
    /**
     * 通过shopId 查询店铺的商品类别列表
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategoryList(long shopId);

    /**
     * 商品类别的批量添加
     * @param productCategoryList
     * @return 返回影响的行数
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    /**
     * 删除商品分类
     * @param productCategoryId
     * @param shopId
     * @return
     */
    int deleteProductCategory(
            @Param("productCategoryId") long productCategoryId,
            @Param("shopId") long shopId);
}
