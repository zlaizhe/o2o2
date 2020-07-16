package com.imooc.o2o.dao;

import com.imooc.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HeadLineDao {

    /**
     * 条件查询头条
     * @param headLineCondition
     * @return
     */
    List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);
}
