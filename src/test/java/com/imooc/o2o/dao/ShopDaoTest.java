package dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopDaoTest extends BaseTest {

    @Autowired
    private ShopDao shopDao;

    @Test
    public void testQueryShopById() {
        Shop shop = shopDao.queryByShodId(1);
        System.out.println(shop);
        System.out.println(shop.getArea());
        System.out.println(shop.getShopCategory());
        System.out.println(shop.getOwner());
    }

    @Test
    public void testQueryShopList() {
        Shop shopCondtion = new Shop();

//        PersonInfo owner = new PersonInfo();
//        owner.setUserId(1L);
//        shopCondtion.setOwner(owner);
//        Area area = new Area();
//        area.setAreaId(1);
//        shopCondtion.setArea(area);

        ShopCategory shopCategory = new ShopCategory();
        ShopCategory parent = new ShopCategory();
        parent.setShopCategoryId(1L);
        shopCategory.setParent(parent);
        shopCondtion.setShopCategory(shopCategory);

        int count = shopDao.queryShopCount(shopCondtion);
        System.out.println(count);
        List<Shop> shopList = shopDao.queryShopList(shopCondtion, 0, count);
        shopList.forEach(System.out::println);
        System.out.println(shopList.size());
    }

    @Test
    @Ignore
    public void testInsertShop() {
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
        shop.setShopName("测试店铺");
        shop.setShopDesc("test");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");

        int effectedRows = shopDao.insertShop(shop);
        System.out.println(effectedRows);
        assertEquals(1, effectedRows);
    }

    @Test
    @Ignore
    public void testUpdateShop() {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopDesc("测试描述");
        shop.setShopAddr("测试地址");
        shop.setLastEditTime(new Date());

        int effectedRows = shopDao.updateShop(shop);
        System.out.println(effectedRows);
        assertEquals(1, effectedRows);
    }
}
