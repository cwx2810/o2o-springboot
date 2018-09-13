package com.imooc.o2o.entity;

import java.util.Date;

/**
 * @author: LieutenantChen
 * @create: 2018-09-03 11:23
 **/
public class WechatAuth {
    private Long wechatAutuId;
    /**
     * 微信账号和公众号关联的Id
     */
    private String openId;
    /**
     * 微信账号创建时间
     */
    private Date createTime;
    /**
     * 用户信息对象，用来得到user_Id
     */
    private PersonInfo personInfo;

    public Long getWechatAutuId() {
        return wechatAutuId;
    }

    public void setWechatAutuId(Long wechatAutuId) {
        this.wechatAutuId = wechatAutuId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}
