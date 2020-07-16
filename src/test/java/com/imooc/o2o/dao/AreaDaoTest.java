package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 继承BaseTest已在测试前初始化IOC容器
 */
public class AreaDaoTest extends BaseTest {
    @Autowired
    private AreaDao areaDao;

    @Test
    public void testQueryArea() {
        List<Area> areas = areaDao.queryArea();
        System.out.println(areas);
        areas.forEach(System.out::println);
        Assert.assertEquals(4, areas.size());
    }
}
