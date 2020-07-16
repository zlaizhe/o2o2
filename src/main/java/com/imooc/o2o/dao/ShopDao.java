package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShopDao {
    /**
     * 通过Shop id查询一个店铺信息
     *
     * @param shopId
     * @return
     */
    Shop queryByShodId(long shopId);

    /**
     * 分页查询店铺列表，可输入条件：店铺名（模糊），店铺状态，店铺类别，区域Id，owner
     *
     * @param shopCondition
     * @param rowIndex      从第几条开始查询
     * @param pageSize      返回的调数
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
                             @Param("rowIndex") int rowIndex,
                             @Param("pageSize") int pageSize);

    /**
     * 返回queryShopList总数（用于分页）
     * @param shopCondition
     * @return
     */
    int queryShopCount(@Param("shopCondition") Shop shopCondition);

    /**
     * 新增一个店铺，返回影响行数
     *
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新一个店铺，返回影响行数
     *
     * @param shop
     * @return
     */
    int updateShop(Shop shop);
}
