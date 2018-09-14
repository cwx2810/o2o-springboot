package com.imooc.o2o.dto;

import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;

import java.util.List;

/**
 * @author: LieutenantChen
 * @create: 2018-09-04 17:27
 **/
public class ShopExecution {
    /**
     * 结果状态
     */
    private int state;
    /**
     * 对结果状态的解释
     */
    private String stateInfo;
    /**
     * 店铺数量
     */
    private int count;
    /**
     * 店铺对象本身，增删改的时候用
     */
    private Shop shop;
    /**
     * 店铺列表对象，查询的时候用
     */
    private List<Shop> shopList;

    // 默认构造方法
    public ShopExecution() {

    }
    // 第2个构造函数，对shop操作失败时用
    public ShopExecution(ShopStateEnum shopStateEnum) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
    }
    // 第3个构造函数，对shop操作成功时用
    public ShopExecution(ShopStateEnum shopStateEnum, Shop shop) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
        this.shop = shop;
    }
    // 第4个构造函数，对shop操作成功时用，返回shop列表
    public ShopExecution(ShopStateEnum shopStateEnum, List<Shop> shopList) {
        this.state = shopStateEnum.getState();
        this.stateInfo = shopStateEnum.getStateInfo();
        this.shopList = shopList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
