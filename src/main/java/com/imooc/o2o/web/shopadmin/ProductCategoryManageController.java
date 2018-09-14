package com.imooc.o2o.web.shopadmin;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.dto.Result;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: LieutenantChen
 * @create: 2018-09-07 22:35
 **/
@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManageController {

    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 获取各个商铺中的商品分类列表
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
    @ResponseBody
    private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest httpServletRequest) {

        // 模拟session
        //Shop shop = new Shop();
        //shop.setShopId(1L);
        //httpServletRequest.getSession().setAttribute("currentShop", shop);

        // 获取自己模拟的塞进去的session
        Shop currentShop = (Shop) httpServletRequest.getSession().getAttribute("currentShop");

        List<ProductCategory> productCategoryList = null;

        // 获取到了用户id，返回查询结果
        if (currentShop != null && currentShop.getShopId() > 0) {
            productCategoryList = productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true, productCategoryList);
        } else {
            // 没有获取到，返回错误码及解释
            ProductCategoryStateEnum productCategoryStateEnum = ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false,
                    productCategoryStateEnum.getState(),
                    productCategoryStateEnum.getStateInfo());
        }
    }

    /**
     * 批量添加商品类别
     * @param productCategoryList
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addProductCategorys(
            @RequestBody List<ProductCategory> productCategoryList,
                         HttpServletRequest httpServletRequest) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Shop currentShop = (Shop) httpServletRequest.getSession().getAttribute("currentShop");
        // 给每个商品分类赋予从session中取出的shopId
        for (ProductCategory productCategory : productCategoryList) {
            productCategory.setShopId(currentShop.getShopId());
        }
        // 如果有商品分类，则添加进去
        if (productCategoryList != null && productCategoryList.size() > 0) {
            try {
                ProductCategoryExecution productCategoryExecution = productCategoryService.batchAddProductCategory(productCategoryList);
                // 添加进去后得到的结果是成功的好说，如果不成功，返回错误原因
                if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productCategoryExecution.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());// 捕获异常
                return modelMap;
            }
        } else {
            modelMap.put("succes", false);
            modelMap.put("errMsg", "请添加一个商品分类");
        }
        return modelMap;
    }

    /**
     * 删除商品类别
     * @param productCategoryId
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> removeProductCategory(
            Long productCategoryId,
            HttpServletRequest httpServletRequest) {
        // 返回集
        Map<String, Object> modelMap = new HashMap<String, Object>();

        // 边界检查
        if (productCategoryId != null && productCategoryId > 0) {
            try {
                Shop currentShop = (Shop) httpServletRequest.getSession().getAttribute("currentShop");
                // 删除操作
                ProductCategoryExecution productCategoryExecution =
                        productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
                // 删除后得到的结果是成功的好说，如果不成功，返回错误原因
                if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productCategoryExecution.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());// 捕获异常
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请选择要删除的商品分类");
        }
        return modelMap;
    }

}
