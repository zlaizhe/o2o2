package com.imooc.o2o.service;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest {
    @Autowired
    private ShopService shopService;

    @Test
    public void testGetShopList() {
        Shop shopCondition = new Shop();
        PersonInfo pi = new PersonInfo();
        pi.setUserId(1L);
        shopCondition.setOwner(pi);
        shopCondition.setShopName("店");
        ShopExecution se = shopService.getShopList(shopCondition, 1, 2);
        System.out.println("这一页店铺列表数：" + se.getShopList().size());
        System.out.println("店铺总数：" + se.getCount());
    }

    @Test
    public void testAddShop() throws FileNotFoundException {
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);

        Area area = new Area();
        area.setAreaId(2);

        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);

        Shop shop = new Shop();
        shop.setArea(area);
        shop.setOwner(owner);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试店铺2");
        shop.setShopDesc("test2");
        shop.setShopAddr("test2");
        shop.setPhone("test2");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");

        //模拟上传的图片
        File shopImg = new File("C:\\USER\\桌面\\xiaohuangren.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder(shopImg.getName(), is);
        ShopExecution se = shopService.addShop(shop, imageHolder);
        System.out.println(se);
        assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
    }

    @Test
    public void testModifyShop() throws FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopName("修改后的店铺");
        shop.setPriority(2);
        File file = new File("C:\\USER\\桌面\\xiaohuangren - 副本.jpg");
        InputStream is = new FileInputStream(file);
        ImageHolder imageHolder = new ImageHolder(file.getName(), is);
        ShopExecution shopExecution = shopService.modifyShop(shop,imageHolder);
        System.out.println(shopExecution.getShop());
    }
}
