package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductImg;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProductImgDao {
    /**
     * 批量添加商品图片
     * @param productImgList
     * @return
     */
    int batchInsertProductImg(List<ProductImg> productImgList);

    /**
     * 删除指定商品的所有详情图
     * @param productId
     * @return
     */
    int deleteProductImgByProductId(long productId);

    /**
     * 查询指定商品的所有详情图
     * @param productId
     * @return
     */
    List<ProductImg> queryProductImgList(long productId);
}
