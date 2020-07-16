package com.imooc.o2o.service;

import com.imooc.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {
    public static final String SCLISTKEY = "shopcategorylist";//商铺类别存储在redis中的key
    /**
     * 条件查询店铺分类，传入null时只查一级分类，传入非null时只查二级分类和更高级分类
     * @param shopCategoryCondition
     * @return
     */
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
