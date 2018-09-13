package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductImg;

import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-08 23:40
 **/
public interface ProductImgDao {


    /**
     * 根据产品id查询产品详情图列表
     * @param productId
     * @return
     */
    List<ProductImg> queryProductImgList(long productId);
    /**
     * 批量添加商品图片
     * @param productImgList
     * @return 插入了多少行图片
     */
    int batchInsertProductImg(List<ProductImg> productImgList);

    /**
     * 根据商品id删除该商品所有详情图
     * @param ProductId
     * @return 返回影响的行数
     */
    int deleteProductImgByProductId(long ProductId);
}
