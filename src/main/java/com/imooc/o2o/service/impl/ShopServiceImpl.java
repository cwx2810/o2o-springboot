package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-04 17:59
 **/
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    /**
     * 分页返回店铺列表
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
        // 获取shopList
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        // 获取总数
        int count = shopDao.queryShopCount(shopCondition);
        // 接收结果
        ShopExecution shopExecution = new ShopExecution();
        if (shopList != null) {
            shopExecution.setShopList(shopList);;
            shopExecution.setCount(count);
        } else {
            shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return shopExecution;
    }

    /**
     * 根据店铺id查询店铺
     * @param shopId
     * @return
     */
    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    /**
     * 更新店铺信息，包括对图片的处理
     * @param shop
     * @param thumbnail
     * @return
     * @throws ShopOperationException
     */
    @Override
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        } else {
            try {
                // 1.如果有传入图片地址，则需要改变图片地址
                if (thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())) {
                    // 获取表格中已经有的img，如果有，则清空
                    if (shopDao.queryByShopId(shop.getShopId()).getShopImg() != null) {
                        ImageUtil.deleteFileOrPath(shopDao.queryByShopId(shop.getShopId()).getShopImg());
                    }
                    // 增加我们设置的新的图片
                    addShopImg(shop, thumbnail);
                }
                // 2.更新店铺信息
                shop.setLastEditTime(new Date());
                // 更新操作，接收一个返回值
                int effectedNumber = shopDao.updateShop(shop);
                if (effectedNumber <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    // 操作成功后，将最新的信息查询一下返回给前台
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            } catch (Exception e) {
                throw new ShopOperationException("更新店铺失败：" + e.getMessage());
            }
        }
    }

    /**
     * 添加店铺
     * @param shop
     * @param thumbnail
     * @return
     */
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
        // 空值判断
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }

        try {
            // 给不能改变的店铺信息赋值
            shop.setEnableStatus(0);// 设置状态为未审核
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());

            // 添加店铺其他信息
            int effectedNumber = shopDao.insertShop(shop);
            if (effectedNumber <= 0) {
                throw new ShopOperationException("插入店铺信息失败");
            } else {
                // 店铺创建成功后，检查传入的图片参数是否存在，存储图片
                if (thumbnail.getImage() != null) {
                    try {
                        // 有图片，则添加图片
                        addShopImg(shop, thumbnail);
                    } catch (Exception e) {
                        throw new ShopOperationException("添加图片失败：" + e.getMessage());
                    }
                    // 添加图片成功后更新图片地址到shop表
                    effectedNumber = shopDao.updateShop(shop);
                    if (effectedNumber <= 0) {
                        throw new ShopOperationException("图片地址更新失败");
                    }
                }
            }
        } catch (Exception e) {
            throw new ShopOperationException("添加店铺失败：" + e.getMessage());
        }

        // 返回我们定义的返回集，即返回插入的shop的状态和状态解释
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    /**
     * 店铺创建成功后插入图片
     * @param shop
     * @param thumbnail
     * @throws IOException
     */
    private void addShopImg(Shop shop, ImageHolder thumbnail) throws IOException {
        String targetAddress = PathUtil.getShopImgPath(shop.getShopId());
        String relativeAddress = ImageUtil.generateThumbnail(thumbnail, targetAddress);
        shop.setShopImg(relativeAddress);
    }
}
