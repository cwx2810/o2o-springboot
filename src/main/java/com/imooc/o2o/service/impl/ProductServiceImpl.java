package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-10 13:57
 **/

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductImgDao productImgDao;


    /**
     * 分页查询商品列表
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
        // 获取shopList
        List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
        // 获取总数
        int count = productDao.queryProductCount(productCondition);
        // 接收结果
        ProductExecution productExecution = new ProductExecution();
        if (productList != null) {
            productExecution.setProductList(productList);
            productExecution.setCount(count);
        } else {
            productExecution.setState(ProductStateEnum.INNER_ERROR.getState());
        }
        return productExecution;
    }

    /**
     * 通过商品Id查询商品
     * @param ProductId
     * @return
     */
    @Override
    public Product getProductById(long ProductId) {
        return productDao.queryProductById(ProductId);
    }

    /**
     * 添加商品信息及图片处理
     * @param product
     * @param thumbnail
     * @param productImgHolderList
     * @return
     * @throws ProductOperationException
     */
    @Override
    @Transactional
    public ProductExecution addProduct(
            Product product, ImageHolder thumbnail,
            List<ImageHolder> productImgHolderList) throws ProductOperationException, IOException {
        // 边界检查
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            // 设置默认属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            product.setEnableStatus(1);
            // 缩略图不为空则添加
            if (thumbnail != null) {
                addThumbnail(product ,thumbnail);
            }
            // 添加商品
            try {
                int effectedNumber = productDao.insertProduct(product);
                if (effectedNumber <= 0) {
                    throw new ProductOperationException("创建商品失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建出错：" + e.getMessage());
            }
            // 详情图不为空则添加
            if (productImgHolderList != null && productImgHolderList.size() > 0) {
                addProductImgList(product, productImgHolderList);
            }
            // 返回添加成功
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        }
        // 边界检查失败
        return new ProductExecution(ProductStateEnum.EMPTY);
    }

    /**
     * 修改商品
     * @param product
     * @param thumbnail
     * @param productImgHolderList
     * @return
     * @throws ProductOperationException
     * @throws IOException
     */
    @Override
    @Transactional
    public ProductExecution modifyProduct(
            Product product, ImageHolder thumbnail,
            List<ImageHolder> productImgHolderList) throws ProductOperationException, IOException {
        // 边界检查
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            // 设置默认属性
            product.setLastEditTime(new Date());
            // 缩略图不为空则添加
            if (thumbnail != null) {
                // 先获取并删除原来有的图
                Product tempProduct = productDao.queryProductById(product.getProductId());
                if (tempProduct.getImgAddr() != null) {
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                // 添加新的图
                addThumbnail(product ,thumbnail);
            }
            // 详情图不为空则添加
            if (productImgHolderList != null && productImgHolderList.size() > 0) {
                // 先删除原先的
                deleteProductImgList(product.getProductId());
                // 添加新的详情图
                addProductImgList(product, productImgHolderList);
            }
            // 添加商品
            try {
                int effectedNumber = productDao.updateProduct(product);
                if (effectedNumber <= 0) {
                    throw new ProductOperationException("更新商品失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("更新出错：" + e.getMessage());
            }

            // 返回更新成功
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        }
        // 边界检查失败
        return new ProductExecution(ProductStateEnum.EMPTY);
    }

    /**
     * 添加缩略图
     * @param product
     * @param thumbnail
     */
    private void addThumbnail(Product product, ImageHolder thumbnail) throws IOException {
        // 获取图片存放路径，这里直接放到对应的店铺底下
        String targetAddress = PathUtil.getShopImgPath(product.getShop().getShopId());
        // 生成缩略图地址
        String thumbnailAddress = ImageUtil.generateThumbnail(thumbnail, targetAddress);
        product.setImgAddr(thumbnailAddress);
    }

    /**
     * 批量添加商品详情图片，大图
     * @param product
     * @param productImgHolderList 待添加的图片列表
     */
    private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) throws IOException {
        // 获取图片存放路径，这里直接放到对应的店铺底下
        String targetAddress = PathUtil.getShopImgPath(product.getShop().getShopId());
        // 图片存放的实体类
        List<ProductImg> productImgList = new ArrayList<ProductImg>();
        // 批处理待添加图片
        for (ImageHolder productImgHolder : productImgHolderList) {
            // 生成正常大小图片的地址
            String normalImgAddress = ImageUtil.generateNormalImg(productImgHolder, targetAddress);

            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(normalImgAddress);
            productImg.setImgDesc("测试非空描述");
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());

            productImgList.add(productImg);
        }
        // 如果确实有图片需要添加，就进行批量添加
        if (productImgList.size() > 0) {
            try {
                int effectedNumber = productImgDao.batchInsertProductImg(productImgList);
                if (effectedNumber <= 0) {
                    throw new ProductOperationException("创建商品详情图片失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建错误：" + e.getMessage());
            }
        }
    }

    /**
     * 删除某个商品下的所有详情图
     * @param productId
     */
    private void deleteProductImgList(long productId) {
        // 获取原来的图片
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        // 干掉具体的图片
        for (ProductImg productImg : productImgList) {
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        // 删除数据库中图片信息
        productImgDao.deleteProductImgByProductId(productId);
    }



}
