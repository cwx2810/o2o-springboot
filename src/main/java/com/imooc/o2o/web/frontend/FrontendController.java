package com.imooc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: LieutenantChen
 * @create: 2018-09-12 15:35
 **/
@Controller
@RequestMapping(value = "/frontend", method = RequestMethod.GET)
public class FrontendController {

    @RequestMapping(value = "/index")
    private String index() {
        return "frontend/index";
    }

    @RequestMapping(value = "/shoplist")
    private String shopList() {
        return "frontend/shoplist";
    }

    @RequestMapping(value = "/shopdetail")
    private String shopDetail() {
        return "frontend/shopdetail";
    }
}
