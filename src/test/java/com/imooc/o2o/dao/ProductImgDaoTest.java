package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductImg;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 继承BaseTest已在测试前初始化IOC容器
 */
public class ProductImgDaoTest extends BaseTest {
    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void testBatchInsertProductImg() {
        ProductImg pi1 = new ProductImg();
        pi1.setImgAddr("图片地址1");
        pi1.setImgDesc("测试图片1");
        pi1.setPriority(1);
        pi1.setCreateTime(new Date());
        pi1.setProductId(1L);

        ProductImg pi2 = new ProductImg();
        pi2.setImgAddr("图片地址2");
        pi2.setImgDesc("测试图片2");
        pi2.setPriority(1);
        pi2.setCreateTime(new Date());
        pi2.setProductId(1L);
        List<ProductImg> list = new ArrayList<>();
        Collections.addAll(list, pi1, pi2);
        int r = productImgDao.batchInsertProductImg(list);
        Assert.assertEquals(2, r);
    }

    @Test
    public void testDeleteProductImgByProductId() {
        long productId = 1;
        int rows = productImgDao.deleteProductImgByProductId(productId);
        Assert.assertEquals(2, rows);
    }
}
