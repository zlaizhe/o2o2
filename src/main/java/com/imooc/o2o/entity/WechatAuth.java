package com.imooc.o2o.entity;

import java.util.Date;

//微信账号
public class WechatAuth {
    private Long wechatAuthId;//id
    private String openId;//开放ID，用于微信登录
    private Date createTime;//创建时间
    private PersonInfo personInfo;//一对一关联用户信息

    public Long getWechatAuthId() {
        return wechatAuthId;
    }

    public void setWechatAuthId(Long wechatAuthId) {
        this.wechatAuthId = wechatAuthId;
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
