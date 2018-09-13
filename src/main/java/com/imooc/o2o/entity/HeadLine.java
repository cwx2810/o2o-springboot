package com.imooc.o2o.entity;

import java.util.Date;

/**
 * @author: LieutenantChen
 * @create: 2018-09-03 19:42
 **/
public class HeadLine {
    private Long lineId;
    /**
     * 头条名称
     */
    private String lineName;
    /**
     * 头条权重
     */
    private Integer priority;
    /**
     * 头条状态：1可用，0禁用
     */
    private Integer enableStatus;
    /**
     * 头条链接
     */
    private String lineLink;
    /**
     * 头条图片
     */
    private String lineImg;

    private Date createTime;
    private Date lastEditTime;

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public String getLineLink() {
        return lineLink;
    }

    public void setLineLink(String lineLink) {
        this.lineLink = lineLink;
    }

    public String getLineImg() {
        return lineImg;
    }

    public void setLineImg(String lineImg) {
        this.lineImg = lineImg;
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
