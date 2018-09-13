package com.imooc.o2o;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: LieutenantChen
 * @create: 2018-09-13 14:46
 **/
@RestController
public class HelloSpringBoot {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello SpringBoot";
    }
}
