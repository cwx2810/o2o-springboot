package com.imooc.o2o.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: LieutenantChen
 * @create: 2018-09-06 11:31
 **/
public class CodeUtil {

    /**
     * 检查验证码是否一致
     * @param httpServletRequest
     * @return
     */
    public static boolean checkVerifyCode(HttpServletRequest httpServletRequest) {
        // 图片中的验证码
        String verifyCodeExpected = (String) httpServletRequest.getSession().getAttribute(
                Constants.KAPTCHA_SESSION_KEY);
        // 我们输入的验证码
        String verifyCodeActual = HttpServletRequestUtil.getString(
                httpServletRequest, "verifyCodeActual");
        if (verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)) {
            return false;
        }
        return true;
    }
}
