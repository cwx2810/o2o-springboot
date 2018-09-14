package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: LieutenantChen
 * @create: 2018-09-05 16:03
 **/
public class HttpServletRequestUtil {
    /**
     * 将浏览器提交上来的请求转换成int
     * @param httpServletRequest
     * @param key
     * @return
     */
    public static int getInt(HttpServletRequest httpServletRequest, String key) {
        try {
            return Integer.decode(httpServletRequest.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }

    public static long getLong(HttpServletRequest httpServletRequest, String key) {
        try {
            return Long.valueOf(httpServletRequest.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }

    public static double getDouble(HttpServletRequest httpServletRequest, String key) {
        try {
            return Double.valueOf(httpServletRequest.getParameter(key));
        } catch (Exception e) {
            return -1d;
        }
    }

    public static boolean getBoolean(HttpServletRequest httpServletRequest, String key) {
        try {
            return Boolean.valueOf(httpServletRequest.getParameter(key));
        } catch (Exception e) {
            return false;
        }
    }

    public static String getString(HttpServletRequest httpServletRequest, String key) {
        try {
            String result = String.valueOf(httpServletRequest.getParameter(key));
            if (result != null) {
                // 如果请求参数不为空，则去掉两边的空格
                result.trim();
            }
            if ("".equals(result)) {
                result = null;
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
