package com.imooc.o2o.entity;

import java.util.Date;

/**
 * @author: LieutenantChen
 * @create: 2018-09-03 17:36
 **/
public class LocalAuth {
    private Long localAuthId;
    /**
     * 用户注册商城的用户名
     */
    private String username;
    /**
     * 注册用的密码
     */
    private String password;
    /**
     * 账号创建时间
     */
    private Date createTime;
    /**
     * 账号最后修改时间
     */
    private Date lastEditTime;
    /**
     * 用户信息对象，用来得到user_Id
     */
    private PersonInfo personInfo;

    public Long getLocalAuthId() {
        return localAuthId;
    }

    public void setLocalAuthId(Long localAuthId) {
        this.localAuthId = localAuthId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}
