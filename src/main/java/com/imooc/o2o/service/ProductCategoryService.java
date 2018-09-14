package com.imooc.o2o.service;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;

import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-07 21:25
 **/
public interface ProductCategoryService {

    /**
     * 查询某个店铺下的所有商品类别列表
     * @param shopId
     * @return
     */
    List<ProductCategory> getProductCategoryList(long shopId);

    /**
     * 批量插入商品的商品分类
     * @param productCategoryList
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductCategoryExecution batchAddProductCategory(
            List<ProductCategory> productCategoryList)
            throws ProductCategoryOperationException;

    /**
     * 删除商品分类
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductCategoryExecution deleteProductCategory(
            long productCategoryId, long shopId)
            throws ProductCategoryOperationException;
}
