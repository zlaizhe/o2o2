package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProductCategoryDao {
    /**
     * 查询一个商铺的所有商品类别
     *
     * @param shopId
     * @return
     */
    public List<ProductCategory> queryProductCategoryList(long shopId);


    /**
     * 批量添加商品类别
     *
     * @param productCategoryList
     * @return 影响的行数
     */
    public int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    /**
     * 删除指定商品类别
     *
     * @param productCategoryId
     * @param shopId
     * @return 影响的行数
     */
    public int deleteProductCategory(@Param("productCategoryId") long productCategoryId,
                                     @Param("shopId") long shopId);
}
