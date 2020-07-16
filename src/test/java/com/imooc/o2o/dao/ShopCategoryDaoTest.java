package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dao.ShopCategoryDao;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopCategoryDaoTest extends BaseTest {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    //@Autowired
    //private ShopCategoryService shopCategoryService;

    @Test
    public void testQueryProductCategoryList() {
        //条件空，查一级分类
        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(null);
        //shopCategoryList = shopCategoryService.getShopCategoryList(null);
        assertEquals(5, shopCategoryList.size());
        System.out.println(shopCategoryList);


        //条件非空，查二级分类
        ShopCategory shopCategoryCondition = new ShopCategory();
        shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
        //shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
        assertEquals(4, shopCategoryList.size());
        System.out.println(shopCategoryList);

        //查父分类id=1的二级分类
        ShopCategory parentCategory = new ShopCategory();
        parentCategory.setShopCategoryId(1L);
        shopCategoryCondition.setParent(parentCategory);
        shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
        //shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
        assertEquals(2, shopCategoryList.size());
        System.out.println(shopCategoryList);
    }


}
