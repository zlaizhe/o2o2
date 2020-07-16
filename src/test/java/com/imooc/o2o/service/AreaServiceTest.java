package com.imooc.o2o.service;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AreaServiceTest extends BaseTest {

    @Autowired
    private AreaService areaService;

    @Autowired
    private CacheService cacheService;

    @Test
    public void testGetAreaList() {
        List<Area> areaList = areaService.getAreaList();
        System.out.println(areaList);
        Assert.assertEquals(4, areaList.size());
        cacheService.removeFromCache(AreaService.AREALISTKEY);//删除redis中的key
        areaList = areaService.getAreaList();
    }

    @Test//测试声明式事务是否回滚
    public void testUpdateArea() {
        int rows = areaService.updateArea(2, false);
        System.out.println(rows);
    }
}
