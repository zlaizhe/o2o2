package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.*;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 继承BaseTest已在测试前初始化IOC容器
 */
public class ProductDaoTest extends BaseTest {
    @Autowired
    private ProductDao productDao;

    @Test
    public void testInsertProduct() {
        Product product = new Product();
        product.setProductName("测试商品");
        product.setProductDesc("测试");
        product.setImgAddr("测试地址");
        product.setNormalPrice("88");
        product.setPromotionPrice("77");
        product.setEnableStatus(0);
        product.setPriority(0);
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());

        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(1L);
        product.setProductCategory(pc);
        Shop shop = new Shop();
        shop.setShopId(1L);
        product.setShop(shop);

        int r = productDao.insertProduct(product);
        Assert.assertEquals(1, r);
    }

    @Test
    public void testQueryProductById() {
        long productId = 4;
        Product product = productDao.queryProductById(productId);
        System.out.println(product);
        System.out.println(product.getShop());
        System.out.println(product.getProductCategory());
        System.out.println(product.getProductImgList());
        Assert.assertEquals(3, product.getProductImgList().size());
    }

    @Test
    public void testUpdateProduct() {

        Product product = new Product();

        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(3L);

        Shop shop = new Shop();
        shop.setShopId(1L);
        product.setShop(shop);
        product.setLastEditTime(new Date());
        product.setProductName("修改的商品");
        product.setProductId(1L);
        product.setProductCategory(pc);

        int r = productDao.updateProduct(product);

        Assert.assertEquals(1, r);
    }

    @Test
    public void testQueryProductList() {
        Product productCondition = new Product();
        productCondition.setProductName("正式");
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(1L);
        productCondition.setShop(shop);
        productCondition.setProductCategory(pc);

        int count = productDao.queryProductCount(productCondition);
        List<Product> productList = productDao.queryProductList(productCondition, 0, count);
        Assert.assertEquals(count, productList.size());
        productList.forEach(System.out::println);
    }

    @Test
    public void testUpdateProductCategoryToNull() {
        int r = productDao.updateProductCategoryToNull(2);
        Assert.assertEquals(1, r);

    }
}
