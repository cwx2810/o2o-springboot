package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-03 23:37
 **/
public interface ShopDao {

    /**
     * 分页查询店铺列表，可输入的条件有：店铺名，店铺状态，店铺类别，区域Id，owner
     * @param shopCondition
     * @param rowIndex 从第几行取数据
     * @param pageSize 返回几行数据
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
                             @Param("rowIndex") int rowIndex,
                             @Param("pageSize") int pageSize);

    /**
     * 获取店铺总数，给分页查询用
     * @param shopCondition
     * @return
     */
    int queryShopCount(@Param("shopCondition") Shop shopCondition);

    /**
     * 通过shopId查询shop
     * @param shopId
     * @return shop
     */
    Shop queryByShopId(long shopId);
    /**
     * 新增店铺
     * @param shop
     * @return 1插入成功，-1插入失败
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺
     * @param shop
     * @return
     */
    int updateShop(Shop shop);
}
