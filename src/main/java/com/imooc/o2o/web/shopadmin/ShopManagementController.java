package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: LieutenantChen
 * @create: 2018-09-05 15:57
 **/
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    /**
     * session操作
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopManagementInfo(HttpServletRequest httpServletRequest) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        long shopId = HttpServletRequestUtil.getLong(httpServletRequest, "shopId");
        // 没获取到shopId，去找session中的shop对象
        if (shopId <= 0) {
            Object currentShopObject = httpServletRequest.getSession().getAttribute("currentShop");
            // 没有当前shop对象，跳转回shop列表
            if (currentShopObject == null) {
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shopadmin/shoplist");
            } else {// 有当前shop对象，设置shopId为这个对象的shopId
                Shop currentShop = (Shop)currentShopObject;
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
            }
        } else {// 获取到了shopId，设置shop对象到session中
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            httpServletRequest.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("redirect", false);
        }
        return modelMap;
    }
    /**
     * 获取某个用户的店铺列表
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopList(HttpServletRequest httpServletRequest) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // 模拟用户登录，把信息保存进session
        PersonInfo user = new PersonInfo();
        user.setUserId(8L);
        httpServletRequest.getSession().setAttribute("user", user);
        // 模拟从session中取得用户信息
        user = (PersonInfo) httpServletRequest.getSession().getAttribute("user");

        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution shopExecution = shopService.getShopList(shopCondition, 0, 100);
            modelMap.put("shopList", shopExecution.getShopList());
            modelMap.put("user", user);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }
    /**
     * 获取店铺，根据店铺id
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest httpServletRequest) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // 用我们写好的方法，从请求中获取shopId
        Long shopId = HttpServletRequestUtil.getLong(httpServletRequest, "shopId");
        if (shopId > -1) {
            try {
                // 通过shopId获取shop信息，还要获取区域列表显示在前台
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "shopId为空");
        }
        return modelMap;
    }

    /**
     * 获取店铺所属类别和区域信息
     * @return 从后台获取数据给modelMap，前台从modelMap读取信息显示下拉列表
     */
    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        List<Area> areaList = new ArrayList<Area>();
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    /**
     * 注册店铺
     * @param httpServletRequest
     * @return 前台提交表单给后台
     */
    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest httpServletRequest) {

        /**
         * 1.接收浏览器请求，转换提交上来的shop和img信息为对象，给shop实体类
         */
        Map<String, Object> modelMap = new HashMap<String, Object>();

        // 验证码
        if (!CodeUtil.checkVerifyCode(httpServletRequest)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        // 接收String
        String shopStr = HttpServletRequestUtil.getString(httpServletRequest, "shopStr");
        // 用fastjson把前端传过来的shopStr转换成shop对象
        ObjectMapper objectMapper = new ObjectMapper();
        Shop shop = null;
        try {
            // 转换成shop对象
            shop = objectMapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        // 接收图片
        CommonsMultipartFile shopImg = null;
        // 获取请求中session中的上下文，用来获取上传的文件流
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(httpServletRequest.getSession().getServletContext());
        // 如果有上传的文件流，就把文件流强制转换为multipart对象，因为这个对象可以让我们提取文件
        if (commonsMultipartResolver.isMultipart(httpServletRequest)) {
            MultipartHttpServletRequest multipartHttpServletRequest =
                    (MultipartHttpServletRequest) httpServletRequest;
            // 提取前端传过来的shopImg
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }

        /**
         * 2.注册店铺
         */
        if (shop != null && shopImg != null) {
            // shop拥有人是不需要前端传的，在session中
            PersonInfo personInfo = (PersonInfo)httpServletRequest.getSession().getAttribute("user");
            shop.setOwner(personInfo);

            // 将shop信息和文件添加到结果集进行判断
            ShopExecution shopExecution;
            try {

                ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
                shopExecution = shopService.addShop(shop, imageHolder);

                // 如果结果集的状态是待审核，则添加成功
                if (shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    // 添加成功后，我们要维护一个session列表，保存该owner可以操作的店铺
                    @SuppressWarnings("unchecked")
                    List<Shop> shopList = (List<Shop>)httpServletRequest.getSession().getAttribute("shopList");
                    if (shopList == null || shopList.size() == 0) {
                        shopList = new ArrayList<Shop>();
                    }
                    shopList.add(shopExecution.getShop());
                    httpServletRequest.getSession().setAttribute("shopList", shopList);

                } else {// 否则添加不成功，返回状态解释
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }

            // 最终返回结果
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "店铺信息为空");
            return modelMap;
        }
    }

    /**
     * 更新店铺
     * @param httpServletRequest
     * @return 前台提交表单给后台
     */
    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest httpServletRequest) {

        /**
         * 1.接收浏览器请求，转换提交上来的shop和img信息为对象，给shop实体类
         */
        Map<String, Object> modelMap = new HashMap<String, Object>();

        // 验证码
        if (!CodeUtil.checkVerifyCode(httpServletRequest)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        // 接收String
        String shopStr = HttpServletRequestUtil.getString(httpServletRequest, "shopStr");
        // 用fastjson把前端传过来的shopStr转换成shop对象
        ObjectMapper objectMapper = new ObjectMapper();
        Shop shop = null;
        try {
            // 转换成shop对象
            shop = objectMapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        // 接收图片
        CommonsMultipartFile shopImg = null;
        // 获取请求中session中的上下文，用来获取上传的文件流
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(httpServletRequest.getSession().getServletContext());
        // 如果有上传的文件流，就把文件流强制转换为multipart对象，因为这个对象可以让我们提取文件
        if (commonsMultipartResolver.isMultipart(httpServletRequest)) {
            MultipartHttpServletRequest multipartHttpServletRequest =
                    (MultipartHttpServletRequest) httpServletRequest;
            // 提取前端传过来的shopImg
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }

        /**
         * 2.更新店铺
         */
        if (shop != null && shop.getShopId() != null) {

            // 将shop信息和文件添加到结果集进行判断
            ShopExecution shopExecution;
            try {
                // 传入的图片为空也行，因为是修改，我们可以不用改图片
                if (shopImg == null) {
                    shopExecution = shopService.modifyShop(shop, null);
                } else {
                    ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
                    shopExecution = shopService.modifyShop(shop, imageHolder);
                }

                // 如果结果集的状态是待审核，则添加成功
                if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {// 否则不成功，返回状态解释
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }

            // 最终返回结果
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "shopId为空");
            return modelMap;
        }
    }

    /**
     * 将Spring流中的文件转换成File文件
     * @param inputStream
     * @param file
     */
//    private static void inputStreamToFile(InputStream inputStream, File file) {
//        FileOutputStream fileOutputStream = null;
//        try {
//            // 不管输入的流是什么类型，输出的流是file类型的
//            fileOutputStream = new FileOutputStream(file);
//            // input的字节个数，初始化为0
//            int bytesRead = 0;
//            // 读取的字节存放在buffer中
//            byte[] buffer = new byte[1024];
//            // buffer中input的字节没有output完（!=-1）就一直output
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                fileOutputStream.write(buffer, 0, bytesRead);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("调用输入流转换文件函数发生异常：" + e.getMessage());
//        } finally {// 用完了关闭流
//            try {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//                if (fileOutputStream != null) {
//                    fileOutputStream.close();
//                }
//            } catch (IOException e) {
//                throw new RuntimeException("关闭流产生异常：" + e.getMessage());
//            }
//        }
//    }
}
