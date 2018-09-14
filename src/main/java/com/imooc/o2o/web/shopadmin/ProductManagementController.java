package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: LieutenantChen
 * @create: 2018-09-10 15:50
 **/
@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    // 支持商品详情图最大上传数量
    private static final int IMAGE_MAX_COUNT = 6;


    /**
     * 分页查询商品信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/getproductlistbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getProductListByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // 获取前台传过来的页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        // 获取前台传过来的每页要求返回的商品数上限
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        // 获取session
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 边界检查
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            // 获取传入的需要检索的条件
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            // 计算出真正查询的条件
            Product productCondition =
                    compactProductCondition4Search(currentShop.getShopId(), productCategoryId, productName);
            // 执行查询
            ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
            // 封装结果
            modelMap.put("productList", pe.getProductList());
            modelMap.put("count", pe.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }
    /**
     * 通过商品Id获取商品信息
     * @param productId
     * @return
     */
    @RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getProductById(@RequestParam Long productId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // 非空判断
        if (productId > -1) {
            // 获取商品
            Product product = productService.getProductById(productId);
            // 获取商品类别
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
            modelMap.put("product", product);
            modelMap.put("productCategoryList", productCategoryList);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品类别id");
        }
        return modelMap;
    }

    /**
     * 添加商品及其缩略图详情图
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/addproduct", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addProduct(HttpServletRequest httpServletRequest) {

        Map<String, Object> modelMap = new HashMap<String, Object>();

        // 验证码校验
        if (!CodeUtil.checkVerifyCode(httpServletRequest)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        // 接收前端变量的初始化
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        String productStr = HttpServletRequestUtil.getString(httpServletRequest, "productStr");
        MultipartHttpServletRequest multipartRequest = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                httpServletRequest.getSession().getServletContext());
        /**
         * 添加详情图
         */
        try {
            // 请求中存在文件流，则取出
            if (multipartResolver.isMultipart(httpServletRequest)) {
                thumbnail = handleImg((MultipartHttpServletRequest) httpServletRequest, productImgList);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空");
                return modelMap;
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        /**
         * 添加商品
         */
        try {
            // 从前端传来的表单中获取String流，将其转换成product实体类
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        // 边界检查
        if (product != null && thumbnail != null && productImgList.size() > 0) {
            try {
                // 从session中获取id
                Shop currentShop = (Shop) httpServletRequest.getSession().getAttribute("currentShop");
                product.setShop(currentShop);

                // 执行添加操作
                ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }

    /**
     * 更新商品
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyProduct(HttpServletRequest httpServletRequest) {
        Map<String, Object> modelMap = new HashMap<String ,Object>();
        // 需要判断是商品更改的时候调用还是只是更新上下架状态
        boolean statusChange = HttpServletRequestUtil.getBoolean(httpServletRequest, "statusChange");
        // 如果是商品更新则进行验证码判断，否则不用验证码
        if (!statusChange && !CodeUtil.checkVerifyCode(httpServletRequest)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        // 接收前端变量的初始化
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        MultipartHttpServletRequest multipartRequest = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                httpServletRequest.getSession().getServletContext());
        /**
         * 更改详情图
         */
        try {
            // 请求中存在文件流，则取出
            if (multipartResolver.isMultipart(httpServletRequest)) {
                thumbnail = handleImg((MultipartHttpServletRequest) httpServletRequest, productImgList);
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        /**
         * 更新商品
         */
        try {
            String productStr = HttpServletRequestUtil.getString(httpServletRequest, "productStr");
            // 从前端传来的表单中获取String流，将其转换成product实体类
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        // 边界检查
        if (product != null) {
            try {
                // 从session中获取id
                Shop currentShop = (Shop) httpServletRequest.getSession().getAttribute("currentShop");
                product.setShop(currentShop);

                // 执行更改操作
                ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }

    /**
     * 图片处理，抽象出的方法
     * @param httpServletRequest
     * @param productImgList
     * @return
     * @throws IOException
     */
    private ImageHolder handleImg(MultipartHttpServletRequest httpServletRequest, List<ImageHolder> productImgList) throws IOException {
        MultipartHttpServletRequest multipartRequest;
        ImageHolder thumbnail = null;
        multipartRequest = httpServletRequest;

        // 从文件流取出缩略图并构建ImageHolder对象
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        if (thumbnailFile != null) {
            thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
        }

        // 取出详情图。构建列表
        for (int i = 0; i < IMAGE_MAX_COUNT; i++) {
            CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest
                    .getFile("productImg" + i);
            if (productImgFile != null) {
                // 若去出的第i个详情图片文件流不为空，则将其加入列表
                ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),
                        productImgFile.getInputStream());
                productImgList.add(productImg);
            } else {
                break;
            }
        }
        return thumbnail;
    }


    /**
     * 组合查询条件
     * @param shopId
     * @param productCategoryId
     * @param productName
     * @return
     */
    private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName) {
        // 初始化商品查询条件
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        // 如果要按类别查询，整合进去
        if (productCategoryId != -1L) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        // 如果要按商品名查询，整合进去
        if (!productName.equals("null") && productName != null) {
            productCondition.setProductName(productName);
        }
        return productCondition;
    }


}
