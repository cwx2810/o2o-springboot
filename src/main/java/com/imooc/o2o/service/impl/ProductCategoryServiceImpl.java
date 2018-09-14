package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-07 21:32
 **/
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Autowired
    private ProductDao productDao;

    /**
     * 根据店铺Id获得该店铺下商品列表
     * @param shopId
     * @return
     */
    @Override
    public List<ProductCategory> getProductCategoryList(long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    /**
     * 批量插入商品的商品分类
     * @param productCategoryList
     * @return
     * @throws ProductCategoryOperationException
     */
    @Override
    @Transactional
    public ProductCategoryExecution batchAddProductCategory(
            List<ProductCategory> productCategoryList)
            throws ProductCategoryOperationException {
        // 商品分类列表不空，还能获取到
        if (productCategoryList != null && productCategoryList.size() > 0) {
            try {
                // 获取一下，返回影响行数
                int effectedNumber = productCategoryDao.batchInsertProductCategory(productCategoryList);
                if (effectedNumber <= 0) {
                    throw new ProductCategoryOperationException("商品类别创建失败，合法行数小于0");
                } else {
                    // 影响行数合法，返回成功提示
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            } catch (Exception e) {
                throw new ProductCategoryOperationException("商品类别创建错误：" + e.getMessage());
            }
        } else {
            // 商品分类列表为空，获取不到，返回空
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }

    }

    /**
     * 删除商品分类
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws ProductCategoryOperationException
     */
    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException {
        // 将此商品类别下的商品的类别id设置为空(解耦)，再删除
        try {
            int effectedNumber = productDao.updateProductCategoryToNull(productCategoryId);
            if (effectedNumber < 0) {
                throw new RuntimeException("商品类别id删除失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("删除商品类别错误：" + e.getMessage());
        }
        try {
            int effectedNumber = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
            if(effectedNumber <= 0) {
                throw new ProductCategoryOperationException("删除失败");
            } else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException("删除发生错误：" + e.getMessage());
        }
    }
}
