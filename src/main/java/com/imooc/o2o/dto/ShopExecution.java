package com.imooc.o2o.dto;

import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;

import java.util.List;

//创建店铺时的结果状态
public class ShopExecution {
    private int state;//结果状态
    private String stateInfo;//状态标识
    private int count;//店铺数量
    private Shop shop;//操作的店铺（增删改时用到）
    private List<Shop> shopList;//店铺列表（查询店铺时用到）

    public ShopExecution() {
    }

    //店铺操作失败时调用
    public ShopExecution(ShopStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    //店铺操作成功时调用
    public ShopExecution(ShopStateEnum stateEnum, Shop shop) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shop = shop;
    }
    //店铺操作成功时调用（操作多个店铺）
    public ShopExecution(ShopStateEnum stateEnum, List<Shop> shopList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopList = shopList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
