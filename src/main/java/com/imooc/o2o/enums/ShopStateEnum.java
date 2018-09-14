package com.imooc.o2o.enums;

/**
 * @author: LieutenantChen
 * @create: 2018-09-04 17:30
 **/
public enum ShopStateEnum {

    CHECK(0, "审核中"),
    OFFLINE(-1, "非法店铺"),
    SUCCESS(1, "操作成功"),
    PASS(2, "通过认证"),
    INNER_ERROR(-1001, "系统内部错误"),
    NULL_SHOPID(-1002, "ShopId为空"),
    NULL_SHOP(-1003, "Shop信息为空");

    private int state;
    private String stateInfo;

    // 构造方法
    private ShopStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 根据传入的state值返回对应的enum提示
     * @param state
     * @return enum
     */
    public static ShopStateEnum stateOf(int state) {
        for (ShopStateEnum shopStateEnum : values()) {
            if (shopStateEnum.getState() == state) {
                return shopStateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

}
