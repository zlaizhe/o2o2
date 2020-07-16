package dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.entity.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductCategoryDaoTest extends BaseTest {
    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    public void testQueryProductCategoryList() {
        List<ProductCategory> productCategories = productCategoryDao.queryProductCategoryList(1);
        System.out.println(productCategories.size());
        for (ProductCategory productCategory : productCategories) {
            System.out.println(productCategory);
        }
    }

    @Test
    public void testBatchInsertProductCategory() {
        ProductCategory pc1 = new ProductCategory();
        pc1.setProductCategoryName("测试商品类别1");
        pc1.setPriority(1);
        pc1.setCreateTime(new Date());
        pc1.setShopId(1L);
        ProductCategory pc2 = new ProductCategory();
        pc2.setProductCategoryName("测试商品类别2");
        pc2.setPriority(2);
        pc2.setCreateTime(new Date());
        pc2.setShopId(1L);
        List<ProductCategory> list = new ArrayList<>();
        list.add(pc1);
        list.add(pc2);
        int rows = productCategoryDao.batchInsertProductCategory(list);
        Assert.assertEquals(2, rows);
    }

    @Test
    public void testDeleteProductCategory() {
        List<ProductCategory> list = productCategoryDao.queryProductCategoryList(1);
        for (ProductCategory pc : list) {
            String name = pc.getProductCategoryName();
            if ("测试商品类别1".equals(name) || "测试商品类别2".equals(name)) {
                int rows = productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), pc.getShopId());
                Assert.assertEquals(1, rows);
            }

        }
    }
}
