package com.imooc.o2o.dto;

/**
 * 封装json对象，返回结果都用它
 * @author: LieutenantChen
 * @create: 2018-09-07 22:37
 **/
public class Result<T> {
    /**
     * 是否成功标志
     */
    private boolean success;
    /**
     * 成功时返回的数据
     */
    private T data;
    /**
     * 错误码
     */
    private int errorCode;
    /**
     * 错误信息，即对错误码的解释
     */
    private String errorMsg;

    // 默认构造函数
    public Result() {

    }

    // 成功返回结果时的构造函数
    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    // 返回失败结果时的构造函数
    public Result(boolean success, int errorCode, String errorMsg) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
