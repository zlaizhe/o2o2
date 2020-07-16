package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProductDao {


    /**
     * 分页查询商品列表，可输入条件：商品名（模糊），商品状态，商铺Id，商品类别
     *
     * @param productCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Product> queryProductList(
            @Param("productCondition") Product productCondition,
            @Param("rowIndex") int rowIndex,
            @Param("pageSize") int pageSize);

    /**
     * 查询商品数量
     *
     * @param productCondition
     * @return
     */
    int queryProductCount(@Param("productCondition") Product productCondition);

    /**
     * 查询一个商品
     *
     * @param productId
     * @return
     */
    Product queryProductById(long productId);

    /**
     * 添加商品
     *
     * @param product
     * @return
     */
    int insertProduct(Product product);

    /**
     * 更新商品信息
     *
     * @param product
     * @return
     */
    int updateProduct(Product product);

    /**
     * 将某一类别的商品的 productCategoryId置为null
     * @param productCategoryId
     * @return
     */
    int updateProductCategoryToNull(long productCategoryId);
}
