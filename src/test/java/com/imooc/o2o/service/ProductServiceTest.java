package com.imooc.o2o.service;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductServiceTest extends BaseTest {

    @Autowired
    private ProductService productService;

    @Test
    public void testAddProduct() throws FileNotFoundException {
        Product product = new Product();
        product.setProductName("测试商品2");
        product.setProductDesc("测试2");
        product.setNormalPrice("99");
        product.setPromotionPrice("66");
        product.setPriority(10);

        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(1L);
        product.setProductCategory(pc);
        Shop shop = new Shop();
        shop.setShopId(1L);
        product.setShop(shop);

        File thumbnail = new File("C:\\USER\\桌面\\图片\\4.jpg");
        ImageHolder ih = new ImageHolder(thumbnail.getName(), new FileInputStream(thumbnail));

        File pi1 = new File("C:\\USER\\桌面\\图片\\2.jpg");
        File pi2 = new File("C:\\USER\\桌面\\图片\\3.jpg");
        List<ImageHolder> list = new ArrayList<>();
        list.add(new ImageHolder(pi1.getName(), new FileInputStream(pi1)));
        list.add(new ImageHolder(pi2.getName(), new FileInputStream(pi2)));
        ProductExecution pe = productService.addProduct(product, ih, list);
        Assert.assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
    }

    @Test
    public void testModifyProduct() throws FileNotFoundException {
        Product product = new Product();
        product.setProductId(2L);
        product.setProductName("正式商品2");
        product.setProductDesc("这是2号商品");
        Shop shop = new Shop();
        shop.setShopId(1L);
        product.setShop(shop);

        File t = new File("C:\\USER\\桌面\\图片\\5.jpg");
        ImageHolder thumbnail = new ImageHolder(t.getName(), new FileInputStream(t));

        List<ImageHolder> productImgList = new ArrayList<>();
        File t2 = new File("C:\\USER\\桌面\\图片\\1.jpg");
        productImgList.add(new ImageHolder(t2.getName(), new FileInputStream(t2)));

        ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
        Assert.assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
    }

}
