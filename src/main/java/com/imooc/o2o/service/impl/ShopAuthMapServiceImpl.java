package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ShopAuthMapDao;
import com.imooc.o2o.dto.ShopAuthMapExecution;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopAuthMapStateEnum;
import com.imooc.o2o.exceptions.ShopAuthMapOperationException;
import com.imooc.o2o.service.ShopAuthMapService;

@Service
public class ShopAuthMapServiceImpl implements ShopAuthMapService {
    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Override
    public ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize) {
        // 空值判断
        if (shopId == null || pageIndex < 1 || pageSize < 1) {
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOPAUTH_ID);
        }
        // 页转行
        int beginIndex = (pageIndex - 1) * pageSize;
        // 查询返回该店铺的授权信息列表
        List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(shopId, beginIndex,
                pageSize);
        // 返回总数
        int count = shopAuthMapDao.queryShopAuthCountByShopId(shopId);
        ShopAuthMapExecution se = new ShopAuthMapExecution();
        se.setShopAuthMapList(shopAuthMapList);
        se.setCount(count);
        return se;
    }

    @Override
    public ShopAuthMap getShopAuthMapById(Long shopAuthId) {
        return shopAuthMapDao.queryShopAuthMapById(shopAuthId);
    }

    @Override
    @Transactional
    public ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
        // 空值判断，主要是对店铺Id和员工Id做校验
        if (shopAuthMap == null || shopAuthMap.getShop() == null || shopAuthMap.getShop().getShopId() == null
                || shopAuthMap.getEmployee() == null || shopAuthMap.getEmployee().getUserId() == null) {
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOPAUTH_INFO);
        }

        shopAuthMap.setCreateTime(new Date());
        shopAuthMap.setLastEditTime(new Date());
        shopAuthMap.setEnableStatus(1);
        // 添加授权信息
        int effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
        if (effectedNum <= 0) {
            throw new ShopAuthMapOperationException("添加授权失败");
        }
        return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
    }

    @Override
    @Transactional
    public ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
        // 空值判断，主要是对授权Id做校验
        if (shopAuthMap == null || shopAuthMap.getShopAuthId() == null) {
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOPAUTH_ID);
        }
        shopAuthMap.setLastEditTime(new Date());
        int effectedNum = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
        if (effectedNum <= 0) {
            throw new ShopAuthMapOperationException("修改授权失败");
        }
        // 创建成功
        return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
    }

}
