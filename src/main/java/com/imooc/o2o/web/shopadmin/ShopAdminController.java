package com.imooc.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: LieutenantChen
 * @create: 2018-09-05 17:59
 **/
@Controller
@RequestMapping(value = "/shopadmin", method = RequestMethod.GET)
public class ShopAdminController {

    /**
     * 跳转到店铺操作
     * @return
     */
    @RequestMapping(value = "/shopoperation")
    public String shopOperation() {
        // 视图解析器上下文我们已经定义过了，所以前面不用写路径文件夹，后面也不用带.html
        return "shop/shopoperation";
    }

    /**
     * 跳转到店铺列表
     * @return
     */
    @RequestMapping(value = "/shoplist")
    public String shopList() {
        // 视图解析器上下文我们已经定义过了，所以前面不用写路径文件夹，后面也不用带.html
        return "shop/shoplist";
    }

    /**
     * 跳转到店铺管理
     * @return
     */
    @RequestMapping(value = "/shopmanage")
    public String shopManage() {
        // 视图解析器上下文我们已经定义过了，所以前面不用写路径文件夹，后面也不用带.html
        return "shop/shopmanage";
    }

    /**
     * 跳转到店铺管理中的商品类别管理
     * @return
     */
    @RequestMapping(value = "/productcategorymanage")
    public String productCategoryManage() {
        // 视图解析器上下文我们已经定义过了，所以前面不用写路径文件夹，后面也不用带.html
        return "shop/productcategorymanage";
    }

    /**
     * 跳转到商品添加页面
     * @return
     */
    @RequestMapping(value = "/productoperation")
    public String productOperation() {
        // 视图解析器上下文我们已经定义过了，所以前面不用写路径文件夹，后面也不用带.html
        return "shop/productoperation";
    }

    /**
     * 跳转到商品管理
     * @return
     */
    @RequestMapping(value = "/productmanage")
    public String productManage() {
        // 视图解析器上下文我们已经定义过了，所以前面不用写路径文件夹，后面也不用带.html
        return "shop/productmanage";
    }
}
