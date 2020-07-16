package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dto.ProductCategoryExectution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public List<ProductCategory> getProductCategoryList(long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    @Transactional
    public ProductCategoryExectution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {
        if (productCategoryList == null || productCategoryList.isEmpty()) {
            return new ProductCategoryExectution(ProductCategoryStateEnum.EMPTY_LIST);
        }
        int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
        if (effectedNum <= 0) {
            throw new ProductCategoryOperationException("商品类别批量添加失败");
        }
        return new ProductCategoryExectution(ProductCategoryStateEnum.SUCCESS);
    }

    @Override
    @Transactional
    public ProductCategoryExectution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException {
        //将此商品类别下的商品的类别id置为空
        int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
        if (effectedNum < 0) {
            throw new ProductCategoryOperationException("商品类别删除失败");
        }
        //删除该商品类别
        effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
        if (effectedNum <= 0) {
            throw new ProductCategoryOperationException("商品类别删除失败");
        }
        return new ProductCategoryExectution(ProductCategoryStateEnum.SUCCESS);
    }
}
