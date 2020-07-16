package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.imooc.o2o.BaseTest;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.entity.Shop;

public class ProductSellDailyDaoTest extends BaseTest {
    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    /**
     * 测试添加功能
     *
     * @throws Exception
     */
    @Test
    public void testAInsertProductSellDaily() throws Exception {
        // 创建商品日销量统计（统计昨天一天整个平台每个商品的日销量，通过tb_user_product_map表统计）
        int effectedNum = productSellDailyDao.insertProductSellDaily();
        assertEquals(3, effectedNum);
    }

    /**
     * 测试添加功能
     *
     * @throws Exception
     */
    @Test
    public void testBInsertDefaultProductSellDaily() throws Exception {
        // 创建商品日销量统计
        int effectedNum = productSellDailyDao.insertDefaultProductSellDaily();
        assertEquals(1, effectedNum);
    }

    /**
     * 测试查询功能
     *
     * @throws Exception
     */
    @Test
    public void testCQueryProductSellDaily() throws Exception {
        ProductSellDaily productSellDaily = new ProductSellDaily();
        // 叠加店铺去查询
        Shop shop = new Shop();
        shop.setShopId(1L);
        productSellDaily.setShop(shop);
        List<ProductSellDaily> productSellDailyList = productSellDailyDao.queryProductSellDailyList(productSellDaily,
                null, null);
        System.out.println(productSellDailyList.size());
    }
}
