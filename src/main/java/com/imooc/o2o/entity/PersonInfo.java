package com.imooc.o2o.entity;

import java.util.Date;

/**
 * @author: LieutenantChen
 * @create: 2018-09-03 11:16
 **/
public class PersonInfo {
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户头像
     */
    private String profileImg;
    /**
     * 用户email
     */
    private String email;
    /**
     * 用户性别
     */
    private String gender;
    /**
     * 是否允许用户使用商城：1允许，0禁止
     */
    private Integer enableStatus;
    /**
     * 用户类型：1顾客，2店家，3超级管理员
     */
    private Integer userType;
    /**
     * 用户创建时间
     */
    private Date createTime;
    /**
     * 用户修改时间
     */
    private Date lastEditTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
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
}
