package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Area;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AreaDao {
    /**
     * 查询所有Area列表
     * @return List<Area>
     */
    List<Area> queryArea();

    //仅供测试声明式事务能否回滚
    @Deprecated
    int updateArea(int areaId);
}
