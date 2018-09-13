package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-08 23:38
 **/
public interface ProductDao {

    /**
     * 分页查询商品列表
     * @param productCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Product> queryProductList(@Param("productCondition") Product productCondition,
                                   @Param("rowIndex") int rowIndex,
                                   @Param("pageSize") int pageSize);

    /**
     * 查询商品总数，是分页的支持方法
     * @param productCondition
     * @return
     */
    int queryProductCount(@Param("productCondition") Product productCondition);
    /**
     * 通过productId查询商品
     * @param productId
     * @return
     */
    Product queryProductById(long productId);
    /**
     * 插入商品
     * @param product
     * @return
     */
    int insertProduct(Product product);

    /**
     * 修改商品
     * @param product
     * @return 返回影响的行数
     */
    int updateProduct(Product product);

    /**
     * 删除商品类别之前将商品类别Id置空
     * @param productCategoryId
     * @return
     */
    int updateProductCategoryToNull(long productCategoryId);

}
