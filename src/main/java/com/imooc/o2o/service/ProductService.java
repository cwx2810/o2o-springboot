package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.exceptions.ProductOperationException;

import java.io.IOException;
import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-10 13:35
 **/
public interface ProductService {

    /**
     * 分页查询商品列表
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);
    /**
     * 根据商品Id获取商品
     * @param ProductId
     * @return
     */
    Product getProductById(long ProductId);
    /**
     * 添加商品信息及图片处理
     * @param product
     * @param thumbnail
     * @param productImgHolderList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduct(
            Product product, ImageHolder thumbnail,
            List<ImageHolder> productImgHolderList) throws ProductOperationException, IOException;

    /**
     * 修改商品信息
     * @param product
     * @param thumbnail
     * @param productImgHolderList
     * @return
     * @throws ProductOperationException
     * @throws IOException
     */
    ProductExecution modifyProduct(
            Product product, ImageHolder thumbnail,
            List<ImageHolder> productImgHolderList) throws ProductOperationException, IOException;
}
