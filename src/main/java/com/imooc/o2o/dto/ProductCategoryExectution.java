package com.imooc.o2o.dto;

import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;

import java.util.List;

public class ProductCategoryExectution {
    private int state;//结果状态
    private String stateInfo;//状态标识
    private int count;//数量
    private ProductCategory productCategory;//操作值（增删改时用到）
    private List<ProductCategory> productCategoryList;//列表（查询店铺时用到）

    //操作失败时调用
    public ProductCategoryExectution(ProductCategoryStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    //操作成功时调用
    public ProductCategoryExectution(ProductCategoryStateEnum stateEnum, ProductCategory productCategory) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.productCategory = productCategory;
    }

    //店铺操作成功时调用（操作多个）
    public ProductCategoryExectution(ProductCategoryStateEnum stateEnum, List<ProductCategory> productCategoryList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.productCategoryList = productCategoryList;
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

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }
}
