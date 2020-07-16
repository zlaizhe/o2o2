package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.HeadLine;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * 继承BaseTest已在测试前初始化IOC容器
 */
public class HeadLineDaoTest extends BaseTest {
    @Autowired
    private HeadLineDao headLineDao;

    @Test
    public void testQueryArea() throws IOException {
        HeadLine headLineCondition = new HeadLine();
        headLineCondition.setEnableStatus(1);
        List<HeadLine> headLineList = headLineDao.queryHeadLine(headLineCondition);
        System.out.println(headLineList);
        Assert.assertEquals(3, headLineList.size());
    }
}
