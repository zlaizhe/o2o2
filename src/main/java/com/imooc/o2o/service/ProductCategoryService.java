package com.imooc.o2o.service;

import com.imooc.o2o.dto.ProductCategoryExectution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryService {

    public List<ProductCategory> getProductCategoryList(long shopId);

    public ProductCategoryExectution batchAddProductCategory(List<ProductCategory> productCategoryList)
            throws ProductCategoryOperationException;

    //将此类别下的商品里的类别id置为空，再删除掉该商品类别
    public ProductCategoryExectution deleteProductCategory(long productCategoryId, long shopId)
            throws ProductCategoryOperationException;
}
