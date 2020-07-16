package com.imooc.o2o.entity;

import java.util.Date;

//店铺
public class Shop {
    private Long shopId;//id
    private String shopName;//店铺名
    private String shopDesc;//店铺描述
    private String shopAddr;//店铺地址
    private String phone;//店铺联系方式
    private String shopImg;//店铺图片
    private Integer priority;//优先级
    private Date createTime;//创建时间
    private Date lastEditTime;//最后修改时间
    private Integer enableStatus;//状态，-1：不可用，0.审核中, 1：可用
    /*
    不可用：店主不能登录店铺发布商品，页面上也不会显示店铺
    审核中：店主可以登录店铺发布商品，页面上不显示店铺
    可用：店主可以登录店铺发布商品，页面显示店铺
     */
    private String advice;//超级管理员给店家的提醒
    private Area area;//店铺所属的区域，一对一关联Area
    private PersonInfo owner;//店主，一对一关联PersonInfo
    private ShopCategory shopCategory;//店铺所属分类，一对一关联ShopCategory

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public String getShopAddr() {
        return shopAddr;
    }

    public void setShopAddr(String shopAddr) {
        this.shopAddr = shopAddr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public PersonInfo getOwner() {
        return owner;
    }

    public void setOwner(PersonInfo owner) {
        this.owner = owner;
    }

    public ShopCategory getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(ShopCategory shopCategory) {
        this.shopCategory = shopCategory;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", shopDesc='" + shopDesc + '\'' +
                ", shopAddr='" + shopAddr + '\'' +
                ", phone='" + phone + '\'' +
                ", shopImg='" + shopImg + '\'' +
                ", priority=" + priority +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                ", enableStatus=" + enableStatus +
                ", advice='" + advice + '\'' +
                '}';
    }
}
