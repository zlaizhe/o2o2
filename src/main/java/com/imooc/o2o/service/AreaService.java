package com.imooc.o2o.service;

import com.imooc.o2o.entity.Area;

import java.util.List;

public interface AreaService {
    public static final String AREALISTKEY = "arealist";//区域信息在redis中的key

    public List<Area> getAreaList();

    @Deprecated//仅用于测试声明式事务是否回滚
    public int updateArea(int areaId, boolean hasException);
}
