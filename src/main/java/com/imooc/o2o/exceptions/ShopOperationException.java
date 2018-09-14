package com.imooc.o2o.exceptions;

/**
 * @author: LieutenantChen
 * @create: 2018-09-05 01:08
 **/
public class ShopOperationException extends RuntimeException {
    /**
     * 构造函数，对RuntimeException的简单封装
     * @param msg
     */
    public ShopOperationException(String msg) {
        super(msg);
    }
}
